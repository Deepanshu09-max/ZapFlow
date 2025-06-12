package com.zapflow.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class LoadTestE2E { // Fixed: was incorrectly named

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .timeout(Duration.ofSeconds(30))
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PRIMARY_BACKEND_URL = "http://localhost:8080";
    private static final String HOOKS_URL = "http://localhost:8081";

    @Test
    void testConcurrentUserRegistrations() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();

        // Create 50 concurrent user registrations
        for (int i = 0; i < 50; i++) {
            final int userId = i;
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Map<String, Object> signupRequest = Map.of(
                        "username", "loadtest" + userId + "@example.com",
                        "password", "password123",
                        "name", "Load Test User " + userId
                    );

                    String jsonBody = objectMapper.writeValueAsString(signupRequest);
                    
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(PRIMARY_BACKEND_URL + "/api/v1/user/signup"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    return response.statusCode() == 200;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }, executor);
            
            futures.add(future);
        }

        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);

        // Verify results
        long successCount = futures.stream()
                .mapToLong(future -> {
                    try {
                        return future.get() ? 1 : 0;
                    } catch (Exception e) {
                        return 0;
                    }
                }).sum();

        System.out.println("Successful registrations: " + successCount + "/50");
        assertTrue(successCount >= 45, "At least 90% of registrations should succeed");

        executor.shutdown();
    }

    @Test
    void testWebhookThroughput() throws Exception {
        // First create a test user and zap
        String token = createTestUserAndZap();
        String testZapId = createTestZap(token);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Long>> futures = new ArrayList<>();

        // Send 100 webhooks concurrently and measure response times
        for (int i = 0; i < 100; i++) {
            final int webhookId = i;
            CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    
                    Map<String, Object> webhookPayload = Map.of(
                        "user", "Load Test",
                        "webhookId", webhookId,
                        "timestamp", System.currentTimeMillis()
                    );

                    String jsonBody = objectMapper.writeValueAsString(webhookPayload);
                    
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(HOOKS_URL + "/hooks/catch/1/" + testZapId))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    
                    long endTime = System.currentTimeMillis();
                    return response.statusCode() == 200 ? (endTime - startTime) : -1L;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1L;
                }
            }, executor);
            
            futures.add(future);
        }

        // Wait for all webhooks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(120, TimeUnit.SECONDS);

        // Analyze performance
        List<Long> responseTimes = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return -1L;
                    }
                })
                .filter(time -> time > 0)
                .sorted()
                .toList();

        if (!responseTimes.isEmpty()) {
            long avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).sum() / responseTimes.size();
            long p95ResponseTime = responseTimes.get((int) (responseTimes.size() * 0.95));
            
            System.out.println("Successful webhooks: " + responseTimes.size() + "/100");
            System.out.println("Average response time: " + avgResponseTime + "ms");
            System.out.println("95th percentile response time: " + p95ResponseTime + "ms");
            
            assertTrue(responseTimes.size() >= 95, "At least 95% of webhooks should succeed");
            assertTrue(avgResponseTime < 1000, "Average response time should be under 1 second");
            assertTrue(p95ResponseTime < 2000, "95th percentile should be under 2 seconds");
        }

        executor.shutdown();
    }

    private String createTestUserAndZap() throws Exception {
        // Create test user
        Map<String, Object> signupRequest = Map.of(
            "username", "loadtest@example.com",
            "password", "password123",
            "name", "Load Test User"
        );

        String signupBody = objectMapper.writeValueAsString(signupRequest);
        HttpRequest signupReq = HttpRequest.newBuilder()
                .uri(URI.create(PRIMARY_BACKEND_URL + "/api/v1/user/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(signupBody))
                .build();

        httpClient.send(signupReq, HttpResponse.BodyHandlers.ofString());

        // Sign in to get token
        Map<String, Object> signinRequest = Map.of(
            "username", "loadtest@example.com",
            "password", "password123"
        );

        String signinBody = objectMapper.writeValueAsString(signinRequest);
        HttpRequest signinReq = HttpRequest.newBuilder()
                .uri(URI.create(PRIMARY_BACKEND_URL + "/api/v1/user/signin"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(signinBody))
                .build();

        HttpResponse<String> signinResponse = httpClient.send(signinReq, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> signinResult = objectMapper.readValue(signinResponse.body(), Map.class);
        return (String) signinResult.get("token");
    }

    private String createTestZap(String token) throws Exception {
        Map<String, Object> zapRequest = Map.of(
            "availableTriggerId", "webhook",
            "triggerMetadata", Map.of(),
            "actions", List.of(
                Map.of(
                    "availableActionId", "email",
                    "actionMetadata", Map.of(
                        "email", "loadtest@example.com",
                        "body", "Load test email"
                    )
                )
            )
        );

        String zapBody = objectMapper.writeValueAsString(zapRequest);
        HttpRequest zapReq = HttpRequest.newBuilder()
                .uri(URI.create(PRIMARY_BACKEND_URL + "/api/v1/zap/"))
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .POST(HttpRequest.BodyPublishers.ofString(zapBody))
                .build();

        HttpResponse<String> zapResponse = httpClient.send(zapReq, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> zapResult = objectMapper.readValue(zapResponse.body(), Map.class);
        return (String) zapResult.get("zapId");
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}