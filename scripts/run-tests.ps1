Write-Host "Running ZapFlow Test Suite" -ForegroundColor Green
Write-Host "===============================" -ForegroundColor Green

# Test Primary Backend
Write-Host "`nTesting Primary Backend..." -ForegroundColor Yellow
cd ../primary-backend

# Test 1: User Signup
Write-Host "`n1. Testing User Signup..." -ForegroundColor Yellow
try {
    $signupBody = @{
        name = "Test User"
        username = "testuser@example.com"
        password = "password123"
    } | ConvertTo-Json

    $signupResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/user/signup" -Method POST -Body $signupBody -ContentType "application/json"
    Write-Host "✅ User Signup: $($signupResponse.StatusCode)" -ForegroundColor Green
    $signupResponse.Content
} catch {
    Write-Host "❌ User Signup failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: User Signin
Write-Host "`n2. Testing User Signin..." -ForegroundColor Yellow
try {
    $signinBody = @{
        username = "testuser@example.com"
        password = "password123"
    } | ConvertTo-Json

    $signinResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/user/signin" -Method POST -Body $signinBody -ContentType "application/json"
    $signinResult = $signinResponse.Content | ConvertFrom-Json
    $token = $signinResult.token
    Write-Host "✅ User Signin: $($signinResponse.StatusCode)" -ForegroundColor Green
    Write-Host "JWT Token: $token" -ForegroundColor Cyan
} catch {
    Write-Host "❌ User Signin failed: $($_.Exception.Message)" -ForegroundColor Red
    return
}

# Test 3: Create Zap
Write-Host "`n3. Testing Create Zap..." -ForegroundColor Yellow
try {
    $zapBody = @{
        availableTriggerId = "webhook"
        triggerMetadata = @{}
        actions = @(
            @{
                availableActionId = "email"
                actionMetadata = @{
                    email = "testuser@example.com"
                    body = "This is a test email from ZapFlow."
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
    Write-Host "✅ Create Zap: $($zapResponse.StatusCode)" -ForegroundColor Green
    Write-Host "Zap ID: $zapId" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Create Zap failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Trigger Webhook
Write-Host "`n4. Testing Trigger Webhook..." -ForegroundColor Yellow
try {
    $webhookBody = @{
        user = "Test User"
        action = "email_trigger"
        data = @{
            message = "This is a test message for the webhook."
            timestamp = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ssZ")
        }
    } | ConvertTo-Json -Depth 3

    $webhookResponse = Invoke-WebRequest -Uri "http://localhost:8081/hooks/catch/1/$zapId" -Method POST -Body $webhookBody -ContentType "application/json"
    Write-Host "✅ Trigger Webhook: $($webhookResponse.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Trigger Webhook failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Get User Zaps
Write-Host "`n5. Testing Get User Zaps..." -ForegroundColor Yellow
try {
    $zapsResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/zap/" -Method GET -Headers $headers
    Write-Host "✅ Get User Zaps: $($zapsResponse.StatusCode)" -ForegroundColor Green
    $zapsResponse.Content
} catch {
    Write-Host "❌ Get User Zaps failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Get Zap by ID
Write-Host "`n6. Testing Get Zap by ID..." -ForegroundColor Yellow
try {
    $getZapResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/zap/$zapId" -Method GET -Headers $headers
    Write-Host "✅ Get Zap by ID: $($getZapResponse.StatusCode)" -ForegroundColor Green
    $getZapResponse.Content
} catch {
    Write-Host "❌ Get Zap by ID failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 Primary Backend Testing Complete!" -ForegroundColor Green

# Test Hooks Service
Write-Host "`nTesting Hooks Service..." -ForegroundColor Yellow
cd ../hooks

# (Add similar tests for Hooks Service here)

Write-Host "`n🎉 Hooks Service Testing Complete!" -ForegroundColor Green

# Test Processor Service
Write-Host "`nTesting Processor Service..." -ForegroundColor Yellow
cd ../processor

# (Add similar tests for Processor Service here)

Write-Host "`n🎉 Processor Service Testing Complete!" -ForegroundColor Green

# Test Worker Service
Write-Host "`nTesting Worker Service..." -ForegroundColor Yellow
cd ../worker

# (Add similar tests for Worker Service here)

Write-Host "`n🎉 Worker Service Testing Complete!" -ForegroundColor Green

Write-Host "`n✅ ZapFlow Test Suite Completed!" -ForegroundColor Green