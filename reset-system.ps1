Write-Host "🧹 Resetting ZapFlow  System..." -ForegroundColor Yellow
Write-Host "===================================" -ForegroundColor Yellow

# Stop all Spring Boot services
Write-Host "`n⏹️ Stopping Spring Boot services..." -ForegroundColor Cyan
# Note: You'll need to manually stop the mvn spring-boot:run processes

# Stop and clean Docker
Write-Host "`n🐳 Cleaning Docker containers and volumes..." -ForegroundColor Cyan
docker-compose down -v
docker volume prune -f

# Clean logs
Write-Host "`n📝 Cleaning logs..." -ForegroundColor Cyan
if (Test-Path "logs") {
    Remove-Item "logs\*" -Force -ErrorAction SilentlyContinue
    Write-Host "✅ Logs cleaned" -ForegroundColor Green
}

# Start fresh Docker services
Write-Host "`n🚀 Starting fresh Docker services..." -ForegroundColor Cyan
docker-compose up -d

Write-Host "`n⏳ Waiting for services to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 20

Write-Host "`n✅ System reset complete!" -ForegroundColor Green
Write-Host "You can now start your Spring Boot services fresh:" -ForegroundColor White
Write-Host "1. cd primary-backend && mvn spring-boot:run" -ForegroundColor Gray
Write-Host "2. cd hooks && mvn spring-boot:run" -ForegroundColor Gray  
Write-Host "3. cd processor && mvn spring-boot:run" -ForegroundColor Gray
Write-Host "4. cd worker && mvn spring-boot:run" -ForegroundColor Gray

Write-Host "`n🎯 All data has been cleared:" -ForegroundColor Cyan
Write-Host "- Database: Empty (tables will be recreated on startup)" -ForegroundColor White
Write-Host "- Kafka: Fresh topics" -ForegroundColor White
Write-Host "- Logs: Cleared" -ForegroundColor White
