Write-Host "Testing ZapFlow Spring Boot API" -ForegroundColor Green # Fixed: removed extra space
Write-Host "====================================" -ForegroundColor Green

# Test 1: Available Triggers
Write-Host "`n1. Testing Available Triggers..." -ForegroundColor Yellow
try {
    $triggersResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/trigger/available" -Method GET
    Write-Host "✅ Available Triggers: $($triggersResponse.StatusCode)" -ForegroundColor Green
    $triggersResponse.Content
} catch {
    Write-Host "❌ Failed to get triggers: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Available Actions
Write-Host "`n2. Testing Available Actions..." -ForegroundColor Yellow
try {
    $actionsResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/action/available" -Method GET
    Write-Host "✅ Available Actions: $($actionsResponse.StatusCode)" -ForegroundColor Green
    $actionsResponse.Content
} catch {
    Write-Host "❌ Failed to get actions: $($_.Exception.Message)" -ForegroundColor Red
}

# Skip signup and go directly to signin
Write-Host "`n3. Testing User Signin (with existing user)..." -ForegroundColor Yellow
try {
    $signinBody = @{
        username = "your-actual-email@gmail.com"
        password = "password123"
    } | ConvertTo-Json

    $signinResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/user/signin" -Method POST -Body $signinBody -ContentType "application/json"
    $signinResult = $signinResponse.Content | ConvertFrom-Json
    $token = $signinResult.token
    Write-Host "✅ User Signin: $($signinResponse.StatusCode)" -ForegroundColor Green
    Write-Host "JWT Token: $token" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Signin failed: $($_.Exception.Message)" -ForegroundColor Red
    return
}

# Test 4: Create Zap with real email
Write-Host "`n4. Testing Zap Creation with Real Email..." -ForegroundColor Yellow
try {
    $zapBody = @{
        availableTriggerId = "webhook"
        triggerMetadata = @{}
        actions = @(
            @{
                availableActionId = "email"
                actionMetadata = @{
                    email = "info.aquadsss@gmail.com"  # Your actual email
                    body = "🎉 Test email from Spring Boot ZapFlow ! This email was sent automatically."
                }
            }
        )
    } | ConvertTo-Json -Depth 3

    $headers = @{
        "Authorization" = $token
        "Content-Type" = "application/json"
    }

    $zapResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/zap/" -Method POST -Body $zapBody -Headers $headers
    $zapResult = $zapResponse.Content | ConvertFrom-Json
    $zapId = $zapResult.zapId
    Write-Host "✅ Zap Creation: $($zapResponse.StatusCode)" -ForegroundColor Green
    Write-Host "Zap ID: $zapId" -ForegroundColor Cyan
    Write-Host "Webhook URL: http://localhost:8081/hooks/catch/1/$zapId" -ForegroundColor Cyan

    # Test 5: Trigger webhook to send email
    Write-Host "`n5. Triggering Webhook to Send Email..." -ForegroundColor Yellow
    $webhookBody = @{
        user = "PowerShell Test"
        action = "email_trigger"
        data = @{
            message = "Testing complete email flow from ZapFlow PowerShell!"
            timestamp = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ssZ")
        }
    } | ConvertTo-Json -Depth 3

    $webhookResponse = Invoke-WebRequest -Uri "http://localhost:8081/hooks/catch/1/$zapId" -Method POST -Body $webhookBody -ContentType "application/json"
    Write-Host "✅ Webhook Triggered: $($webhookResponse.StatusCode)" -ForegroundColor Green
    Write-Host "Check your email inbox for the ZapFlow test message!" -ForegroundColor Cyan
    Write-Host "Also check worker service logs for email delivery confirmation!" -ForegroundColor Yellow

} catch {
    Write-Host "❌ Zap creation failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 ZapFlow API Testing Complete!" -ForegroundColor Green
Write-Host "Next: Start hooks service with 'cd hooks && mvn spring-boot:run'" -ForegroundColor Yellow
