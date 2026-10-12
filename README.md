# Tourist Microservice Load Balancing and Service Discovery

A minimal Spring Boot project demonstrating Netflix Eureka service discovery and Spring Cloud Gateway load balancing.

## Applications
- `eureka-server`: Eureka registry at `http://localhost:8761`
- `tourist-service`: REST service registered as `TOURIST-SERVICE`; run two separate processes with dynamic ports
- `api-gateway`: Gateway at `http://localhost:8080`, routing `/tourist` through Eureka and Spring Cloud LoadBalancer

## Requirements
- Java 21
- Maven 3.9+
- Internet access on first build

Spring Boot `3.4.5` and Spring Cloud `2024.0.1` are used together.

## Build
Build each project separately with `mvn clean package` from each application folder.

## Run
1. Start `eureka-server` (`mvn spring-boot:run`) and open `http://localhost:8761`.
2. Start the first Tourist Service process (`cd tourist-service; mvn spring-boot:run`).
3. Start a second Tourist Service process in another terminal using `mvn spring-boot:run "-Dspring-boot.run.jvmArguments=-DINSTANCE_ID=tourist-two"` from `tourist-service`.
4. Start the API Gateway (`cd api-gateway; mvn spring-boot:run`).
5. Wait briefly for both service instances to appear in Eureka.

## Test
Send repeated requests to `http://localhost:8080/tourist`. The JSON response identifies the service instance and its dynamically allocated port.

PowerShell:
```powershell
1..10 | ForEach-Object { Invoke-RestMethod http://localhost:8080/tourist | ConvertTo-Json -Compress }
```

The gateway uses `lb://TOURIST-SERVICE`; Spring Cloud LoadBalancer selects instances using round-robin. If no instance is available, the gateway commonly responds with `503 Service Unavailable`.

## GitHub
This repository contains the project source and instructions. Build and run locally before submitting. Do not commit generated `target/` directories.
