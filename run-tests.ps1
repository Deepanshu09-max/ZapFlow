Write-Host "Running ZapFlow  Test Suite" -ForegroundColor Green
Write-Host "===============================" -ForegroundColor Green

# Test Primary Backend
Write-Host "`nTesting Primary Backend..." -ForegroundColor Yellow
cd primary-backend
mvn test -Dspring.profiles.active=test
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Primary Backend tests failed" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Primary Backend tests passed" -ForegroundColor Green

# Test Hooks Service
Write-Host "`nTesting Hooks Service..." -ForegroundColor Yellow
cd ../hooks
mvn test -Dspring.profiles.active=test
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Hooks Service tests failed" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Hooks Service tests passed" -ForegroundColor Green

# Test Processor Service
Write-Host "`nTesting Processor Service..." -ForegroundColor Yellow
cd ../processor
mvn test -Dspring.profiles.active=test
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Processor Service tests failed" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Processor Service tests passed" -ForegroundColor Green

# Test Worker Service
Write-Host "`nTesting Worker Service..." -ForegroundColor Yellow
cd ../worker
mvn test -Dspring.profiles.active=test
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Worker Service tests failed" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Worker Service tests passed" -ForegroundColor Green

Write-Host "`n🎉 All tests passed successfully!" -ForegroundColor Green
Write-Host "Ready for production deployment!" -ForegroundColor Cyan
