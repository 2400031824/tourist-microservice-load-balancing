# Project Summary

## Objective
Demonstrate service discovery and client-side load balancing for a Tourist REST endpoint.

## Components
- **Eureka Server** — registry and dashboard on port 8761.
- **Tourist Service** — REST endpoint `GET /tourist`, registered under `TOURIST-SERVICE`, dynamic port.
- **API Gateway** — port 8080; routes `/tourist` to `lb://TOURIST-SERVICE`.
- **Spring Cloud LoadBalancer** — selects a registered service instance, using round-robin by default.

## Expected flow
Client → Gateway (:8080) → Eureka-discovered Tourist Service instance → JSON response.

## Verification
1. Two service instances appear in the Eureka dashboard.
2. Gateway requests to `http://localhost:8080/tourist` return instance metadata.
3. Repeated calls are distributed across the registered instances.
4. With no service instances available, the gateway reports service unavailable.

See README.md for setup and BUILD_AND_TEST.md for the step-by-step test plan.
