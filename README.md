# Utility Billing Management System

## Overview
Backend API for Water and Sanitation Corporation (WASAC) and Rwanda Energy Group (REG).

## Prerequisites
- Java 21
- Maven
- PostgreSQL

## Database Setup
1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE utility_billing;
   ```
2. The application will automatically create tables on startup.
3. To execute triggers and stored procedures:
   ```bash
   psql -U postgres -d utility_billing -f database/scripts/routines.sql
   ```

## Environment Variables
- `DB_HOST` (default: localhost)
- `DB_PORT` (default: 5432)
- `DB_NAME` (default: utility_billing)
- `DB_USERNAME` (default: postgres)
- `DB_PASSWORD` (default: postgres)

## Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

## Swagger Usage
- URL: `http://localhost:8080/swagger-ui.html`
- Authentication: Use the `Authorize` button and paste the JWT token (prefix with nothing, just the token).

## Sample Accounts
- Admin: `admin@wasac.gov.rw` / `admin123`
- Operator: `operator@wasac.gov.rw` / `operator123`

## Postman Testing Guide
1. Login to get token.
2. Register/Create Customer.
3. Create Meter for Customer.
4. Capture Reading (Operator only).
5. Generate Bill (Admin only).
6. Approve Bill (Finance/Admin only).
7. Record Payment (Finance only).
