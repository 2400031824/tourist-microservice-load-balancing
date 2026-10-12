# Build and Test Guide

## Prerequisites
- JDK 21 (`java -version`)
- Maven 3.9+ (`mvn -version`)
- Network access for the first dependency download

## Build each service
Run in separate commands from the repository root:

```bash
cd eureka-server
mvn clean package
cd ../tourist-service
mvn clean package
cd ../api-gateway
mvn clean package
```

A successful build ends with `BUILD SUCCESS` for each application.

## Start-up sequence
Use four terminal windows and keep each process running.

1. `cd eureka-server && mvn spring-boot:run`
2. `cd tourist-service && mvn spring-boot:run`
3. In another terminal: `cd tourist-service && mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=0"`
4. `cd api-gateway && mvn spring-boot:run`

Open http://localhost:8761 and confirm that `TOURIST-SERVICE` has two registered instances. The gateway itself does not need to register for the required route to work.

## Test checklist

### 1. Eureka dashboard
Open http://localhost:8761. Verify both Tourist Service instances are listed.

### 2. Direct health check
The Tourist Service includes Spring Boot Actuator health support. Since ports are dynamically assigned, find each actual port in its startup log (look for `Tomcat started on port`) and call `http://localhost:<port>/actuator/health`.

### 3. Gateway endpoint
Call `GET http://localhost:8080/tourist`. Expect JSON containing `service`, `instanceId`, `port`, and `message`.

### 4. Load balancing
Make several requests to `http://localhost:8080/tourist`. With two healthy registered instances, the returned instance IDs/ports should alternate across requests under normal round-robin behavior.

### 5. Discovery failure
Stop both Tourist Service processes and call the gateway endpoint again. Expect a gateway service-unavailable response (typically HTTP 503). Restart the service processes and retry after Eureka registration completes.

## Troubleshooting
- **Gateway returns 503:** Confirm Eureka is running, the Tourist Service instances are listed, and the service name is `TOURIST-SERVICE`.
- **Port conflict:** The Eureka server uses 8761 and the gateway uses 8080. Tourist Service uses a random port.
- **Second instance replaces the first:** Ensure each instance has a unique ID. This project uses `eureka.instance.instance-id: ${spring.application.name}:${random.value}`.
- **Dependency resolution fails:** Check internet connectivity and ensure Java 21/Maven are available.
- **Gateway dependency error:** Do not add `spring-boot-starter-web` to the Gateway app; Spring Cloud Gateway uses the reactive WebFlux stack.
