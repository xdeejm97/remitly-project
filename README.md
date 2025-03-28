# Swift Code Service

## Overview
This Spring Boot application provides a comprehensive Swift Code lookup service, allowing users to retrieve bank Swift codes, find codes by country, and manage Swift code entries.

## Prerequisites
- Docker
- Java 17 or later
- Maven

## Running the Application

### 1. Build and Run with Docker Compose
```bash
# Build and start the application and database
docker compose up --build
```

### 2. Accessing the Application
- **Application URL**: `http://localhost:8080`

## API Endpoints

### Retrieve Swift Code
- `GET /v1/swift-codes/{swift-code}`
    - Retrieve details for a specific Swift code
    - Supports both headquarters and branch codes

### Get Swift Codes by Country
- `GET /v1/swift-codes/country/{countryISO2code}`
    - Retrieve all Swift codes for a specific country

### Add Swift Code
- `POST /v1/swift-codes`
    - Add a new Swift code entry

### Delete Swift Code
- `DELETE /v1/swift-codes/{swift-code}`
    - Remove a Swift code entry