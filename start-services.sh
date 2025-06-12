#!/bin/bash

echo "Starting ZapFlow  - Spring Boot Implementation"
echo "=================================================="

# Start Docker dependencies
echo "Starting Docker dependencies..."
docker-compose up -d

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 10

# Start Spring Boot services in background
echo "Starting Primary Backend (Port 8080)..."
cd primary-backend && mvn spring-boot:run > ../logs/primary-backend.log 2>&1 &

echo "Starting Hooks Service (Port 8081)..."
cd ../hooks && mvn spring-boot:run > ../logs/hooks.log 2>&1 &

echo "Starting Processor Service (Port 8082)..."
cd ../processor && mvn spring-boot:run > ../logs/processor.log 2>&1 &

echo "Starting Worker Service (Port 8083)..."
cd ../worker && mvn spring-boot:run > ../logs/worker.log 2>&1 &

# Start Frontend
echo "Starting Frontend (Port 3000)..."
cd ../frontend && npm run dev > ../logs/frontend.log 2>&1 &

echo ""
echo "All services starting..."
echo "Frontend: http://localhost:3000"
echo "Primary Backend: http://localhost:8080"
echo "Hooks: http://localhost:8081"
echo ""
echo "Check logs in the logs/ directory"
echo "To stop all services, run: ./stop-services.sh"
