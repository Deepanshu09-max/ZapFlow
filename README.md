# ZapFlow

A microservices-based automation platform similar to Zapier, built with Spring Boot and Kafka. 

## Architecture

ZapFlow consists of 4 microservices:

- **Primary Backend** (Port 8080) - User management, Zap CRUD operations
- **Hooks Service** (Port 8081) - Webhook handling and ZapRun creation
- **Processor Service** (Port 8082) - Outbox pattern implementation with Kafka
- **Worker Service** (Port 8083) - Action execution (Email, Solana)

## Tech Stack

- Java 17
- Spring Boot 3.1.5
- PostgreSQL
- Apache Kafka
- Redis
- Docker & Docker Compose
- Maven

## Quick Start

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### 1. Clone and Setup

```bash
git clone <repository-url>
cd ZapFlow
```

### 2. Environment Variables

Create `.env` file:

```bash
JWT_SECRET=your-super-secret-jwt-key-at-least-32-characters-long
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-gmail-app-password
```

### 3. Start Infrastructure

```bash
# Start PostgreSQL, Kafka, Zookeeper, Redis
docker-compose up -d

# Create database
docker exec -it zapflow-postgres psql -U postgres -c "CREATE DATABASE zapflow_db;"
```

### 4. Start Services

```bash
# Terminal 1 - Primary Backend
cd primary-backend && mvn spring-boot:run

# Terminal 2 - Hooks Service
cd hooks && mvn spring-boot:run

# Terminal 3 - Processor Service
cd processor && mvn spring-boot:run

# Terminal 4 - Worker Service
cd worker && mvn spring-boot:run
```

## API Endpoints

### User Management

- `POST /api/v1/user/signup` - User registration
- `POST /api/v1/user/signin` - User login

### Zap Management

- `POST /api/v1/zap/` - Create a Zap
- `GET /api/v1/zap/` - Get user's Zaps
- `GET /api/v1/zap/{zapId}` - Get specific Zap

### Webhooks

- `POST /hooks/catch/{userId}/{zapId}` - Trigger webhook

### Available Actions

- `GET /api/v1/action/available` - List available actions
- `GET /api/v1/trigger/available` - List available triggers

## Testing

```bash
# Run tests for all services
mvn test

# Run E2E tests
cd e2e-tests && mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

MIT License
