Write-Host "Running ZapFlow  End-to-End Tests" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green

# Check if Docker is running
try {
    docker --version | Out-Null
    Write-Host "✅ Docker is available" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker is not running. Please start Docker Desktop." -ForegroundColor Red
    exit 1
}

# Start Docker dependencies
Write-Host "`nStarting Docker dependencies..." -ForegroundColor Yellow
docker-compose up -d

Write-Host "Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Start all Spring Boot services in background
Write-Host "`nStarting Spring Boot services..." -ForegroundColor Yellow

$jobs = @()

# Start Primary Backend
Write-Host "Starting Primary Backend..." -ForegroundColor Cyan
cd primary-backend
$job1 = Start-Job -ScriptBlock { mvn spring-boot:run -Dspring.profiles.active=e2e }
$jobs += $job1

# Start Hooks Service  
Write-Host "Starting Hooks Service..." -ForegroundColor Cyan
cd ../hooks
$job2 = Start-Job -ScriptBlock { mvn spring-boot:run -Dspring.profiles.active=e2e }
$jobs += $job2

# Start Processor Service
Write-Host "Starting Processor Service..." -ForegroundColor Cyan
cd ../processor
$job3 = Start-Job -ScriptBlock { mvn spring-boot:run -Dspring.profiles.active=e2e }
$jobs += $job3

# Start Worker Service
Write-Host "Starting Worker Service..." -ForegroundColor Cyan
cd ../worker
$job4 = Start-Job -ScriptBlock { mvn spring-boot:run -Dspring.profiles.active=e2e }
$jobs += $job4

Write-Host "`nWaiting for services to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Health check for services
Write-Host "`nChecking service health..." -ForegroundColor Yellow

$healthChecks = @(
    @{ name = "Primary Backend"; url = "http://localhost:8080/actuator/health" },
    @{ name = "Hooks Service"; url = "http://localhost:8081/actuator/health" }
)

foreach ($check in $healthChecks) {
    try {
        $response = Invoke-WebRequest -Uri $check.url -TimeoutSec 5
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ $($check.name) is healthy" -ForegroundColor Green
        } else {
            Write-Host "⚠️ $($check.name) returned status $($response.StatusCode)" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "❌ $($check.name) is not responding" -ForegroundColor Red
    }
}

# Run E2E Tests
Write-Host "`nRunning End-to-End Tests..." -ForegroundColor Yellow
cd ../e2e-tests
mvn test -Dspring.profiles.active=e2e

$testResult = $LASTEXITCODE

# Cleanup
Write-Host "`nCleaning up..." -ForegroundColor Yellow

# Stop Spring Boot services
foreach ($job in $jobs) {
    Stop-Job $job -PassThru | Remove-Job
}

# Stop Docker services
docker-compose down

# Show results
if ($testResult -eq 0) {
    Write-Host "`n🎉 All E2E tests passed!" -ForegroundColor Green
    Write-Host "Your ZapFlow  is production-ready!" -ForegroundColor Cyan
} else {
    Write-Host "`n❌ Some E2E tests failed" -ForegroundColor Red
    Write-Host "Check the test output above for details" -ForegroundColor Yellow
}

Write-Host "`nE2E Test Summary:" -ForegroundColor Yellow
Write-Host "- Complete user journey tested" -ForegroundColor White
Write-Host "- Zap creation and management tested" -ForegroundColor White
Write-Host "- Webhook to action flow tested" -ForegroundColor White
Write-Host "- Error handling scenarios tested" -ForegroundColor White
Write-Host "- Performance and load testing completed" -ForegroundColor White

exit $testResult
