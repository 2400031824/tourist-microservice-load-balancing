# Quick Start

1. Install Java 21 and Maven 3.9+.
2. Start Eureka: from `eureka-server`, run `mvn spring-boot:run`.
3. Start Tourist Service twice from `tourist-service` in two terminals, running `mvn spring-boot:run` in each.
4. Start the gateway from `api-gateway` with `mvn spring-boot:run`.
5. Open http://localhost:8761 and confirm two `TOURIST-SERVICE` instances.
6. Call http://localhost:8080/tourist repeatedly and compare `instanceId` and `port`.

All three projects use separate Maven builds. See README.md for architecture and BUILD_AND_TEST.md for verification and troubleshooting.
