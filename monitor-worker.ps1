Write-Host "Monitoring Worker Service Logs..." -ForegroundColor Green
Write-Host "=================================" -ForegroundColor Green

# Check if logs directory exists
if (!(Test-Path "logs")) {
    Write-Host "Logs directory not found. Worker service may be running in terminal." -ForegroundColor Yellow
    Write-Host "Check the terminal where you ran 'mvn spring-boot:run' in the worker directory." -ForegroundColor Yellow
    exit
}

# Monitor worker logs
if (Test-Path "logs/worker.log") {
    Write-Host "Following worker.log file..." -ForegroundColor Green
    Get-Content "logs/worker.log" -Tail 20 -Wait
} else {
    Write-Host "worker.log not found. Checking for other log files..." -ForegroundColor Yellow
    Get-ChildItem "logs" -Filter "*.log"
}
