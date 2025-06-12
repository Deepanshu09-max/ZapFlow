# ZapFlow Scripts

This directory contains PowerShell scripts for automating various development and testing tasks.

## 🧪 Testing Scripts

### `test-api.ps1`
Tests the complete API workflow:
- Available triggers and actions
- User signup/signin flow
- Zap creation and management
- Webhook triggering
- Email delivery verification

```powershell
.\scripts\test-api.ps1
```

### `run-tests.ps1`
Runs unit tests for all microservices:
```powershell
.\scripts\run-tests.ps1
```

### `run-e2e-tests.ps1`
Comprehensive end-to-end testing:
```powershell
.\scripts\run-e2e-tests.ps1
```

### `test-coverage.ps1`
Generates test coverage reports:
```powershell
.\scripts\test-coverage.ps1
```

## ⚡ Performance Scripts

### `performance-benchmark.ps1`
Load testing and performance benchmarks:
```powershell
.\scripts\performance-benchmark.ps1
```

## 🔧 Monitoring Scripts

### `monitor-worker.ps1`
Real-time worker service monitoring:
```powershell
.\scripts\monitor-worker.ps1
```

## 🧹 Utility Scripts

### `reset-system.ps1`
Resets the entire development environment:
```powershell
.\scripts\reset-system.ps1
```

## Usage from Root Directory

All scripts can be run from the project root:

```powershell
# Test the API
.\scripts\test-api.ps1

# Run all tests
.\scripts\run-tests.ps1

# Performance testing
.\scripts\performance-benchmark.ps1

# Monitor services
.\scripts\monitor-worker.ps1
```
