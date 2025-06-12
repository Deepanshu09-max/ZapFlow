package com.zapflow.primarybackend.performance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
public class LoadTest {
    
    @Test
    public void testConcurrentUserRegistration() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        CompletableFuture<?>[] futures = IntStream.range(0, 100)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    // Simulate user registration
                    try {
                        Thread.sleep(100);
                        // Add actual HTTP request here
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, executor))
                .toArray(CompletableFuture[]::new);
        
        CompletableFuture.allOf(futures).join();
        executor.shutdown();
    }
}