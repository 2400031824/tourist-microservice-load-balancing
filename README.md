# Tourist Microservice Load Balancing

A lab-ready Spring Boot microservices example showing **Netflix Eureka service discovery**, **Spring Cloud Gateway**, and **Spring Cloud LoadBalancer** with two Tourist Service instances on dynamically assigned ports.

## Architecture

```text
                         ┌──────────────────────┐
Client ── GET /tourist ─▶│ API Gateway :8080    │
                         │ route: lb://TOURIST-SERVICE
                         └──────────┬───────────┘
                                    │ Eureka discovery + round robin
                 ┌──────────────────┴───────────────────┐
                 ▼                                      ▼
       ┌────────────────────┐                 ┌────────────────────┐
       │ Tourist instance 1 │                 │ Tourist instance 2 │
       │ random port        │                 │ random port        │
       └──────────┬─────────┘                 └──────────┬─────────┘
                  └──────────────────┬───────────────────┘
                                     ▼
                         ┌──────────────────────┐
                         │ Eureka :8761         │
                         │ Service registry     │
                         └──────────────────────┘
```

## Technology

- Java 21
- Spring Boot 3.3.0
- Spring Cloud 2023.0.0 (release train compatible with Spring Boot 3.3.x)
- Maven 3.9+
- Netflix Eureka Server and Client
- Spring Cloud Gateway and Spring Cloud LoadBalancer

Each application is an independent Maven project so it can be built and started separately.

## Project structure

```text
.
├── eureka-server/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/eureka/EurekaServerApplication.java
│       └── resources/application.yml
├── tourist-service/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/tourist/TouristServiceApplication.java
│       ├── java/com/example/tourist/TouristController.java
│       ├── java/com/example/tourist/TouristResponse.java
│       └── resources/application.yml
├── api-gateway/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/gateway/ApiGatewayApplication.java
│       └── resources/application.yml
├── BUILD_AND_TEST.md
├── QUICK_START.md
├── PROJECT_SUMMARY.md
├── .gitignore
└── LICENSE
```

## Build

Run each command from the corresponding application directory:

```bash
cd eureka-server && mvn clean package
cd ../tourist-service && mvn clean package
cd ../api-gateway && mvn clean package
```

Java 21 and Maven must be installed. The first build needs internet access to download dependencies.

## Run (four terminals)

Start services in this order:

1. **Eureka Server** — from `eureka-server/`: `mvn spring-boot:run`
   - Dashboard: http://localhost:8761
2. **Tourist Service instance 1** — from `tourist-service/`: `mvn spring-boot:run`
3. **Tourist Service instance 2** — open another terminal in `tourist-service/` and run:
   - Windows PowerShell: `mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=0"`
   - macOS/Linux: `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=0`
   - Both instances use port `0` by default, so the OS assigns distinct available ports. The instance ID includes a random value to avoid collisions.
4. **API Gateway** — from `api-gateway/`: `mvn spring-boot:run`

Wait for the two `TOURIST-SERVICE` instances to appear in the Eureka dashboard.

## Test load balancing

Send repeated requests through the gateway (do not call the service's random ports directly):

```bash
curl http://localhost:8080/tourist
curl http://localhost:8080/tourist
```

PowerShell:

```powershell
1..10 | ForEach-Object {
  Invoke-RestMethod http://localhost:8080/tourist | ConvertTo-Json -Compress
}
```

Each response includes the service name, instance ID, and actual port. With both instances registered, repeated calls should be distributed across instances using Spring Cloud LoadBalancer's round-robin strategy. Exact request order can vary while instances register or expire.

## Discovery failure behavior

If no Tourist Service instance is registered or reachable, the gateway cannot resolve `lb://TOURIST-SERVICE` and normally returns HTTP `503 Service Unavailable`. Start Eureka first, then the service instances, and allow registration to complete before testing.

## Important notes

- Eureka is configured on port **8761** (the lab prompt's `87614` appears to be a typo).
- Do not commit Maven `target/` directories or IDE metadata.
- This repository includes implementation and test instructions; build and run locally to verify behavior in your environment.
