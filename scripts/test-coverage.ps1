Write-Host "ZapFlow Test Coverage Analysis" -ForegroundColor Green
Write-Host "==============================" -ForegroundColor Green

# Run tests with coverage for all services
$services = @("primary-backend", "hooks", "processor", "worker")

foreach ($service in $services) {
    Write-Host "`nAnalyzing coverage for $service..." -ForegroundColor Yellow
    cd ../$service
    mvn clean test jacoco:report
    
    if (Test-Path "target/site/jacoco/index.html") {
        Write-Host "✅ Coverage report generated for $service" -ForegroundColor Green
    } else {
        Write-Host "❌ Coverage report failed for $service" -ForegroundColor Red
    }
}

Write-Host "`n📊 Coverage reports available at:" -ForegroundColor Cyan
foreach ($service in $services) {
    Write-Host "  - $service/target/site/jacoco/index.html" -ForegroundColor White
}
