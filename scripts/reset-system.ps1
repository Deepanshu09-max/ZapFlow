Write-Host "ZapFlow System Reset" -ForegroundColor Red
Write-Host "===================" -ForegroundColor Red

Write-Host "⚠️  This will reset your entire ZapFlow development environment!" -ForegroundColor Yellow
$confirm = Read-Host "Are you sure? (y/N)"

if ($confirm -eq "y" -or $confirm -eq "Y") {
    Write-Host "`nStopping all services..." -ForegroundColor Yellow
    
    # Stop Docker services
    cd ..
    docker-compose down -v
    
    # Clean up logs
    Remove-Item -Path "*/logs/*" -Force -Recurse -ErrorAction SilentlyContinue
    
    # Clean Maven targets
    Remove-Item -Path "*/target" -Force -Recurse -ErrorAction SilentlyContinue
    
    Write-Host "✅ System reset complete!" -ForegroundColor Green
} else {
    Write-Host "Reset cancelled." -ForegroundColor Yellow
}
