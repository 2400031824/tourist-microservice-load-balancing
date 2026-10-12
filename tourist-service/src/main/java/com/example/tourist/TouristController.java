package com.example.tourist;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
public class TouristController {
    private final Environment environment;

    public TouristController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/tourist")
    public TouristResponse getTouristService() {
        String instanceId = environment.getProperty("eureka.instance.instance-id", "tourist-service");
        String port = environment.getProperty("local.server.port", environment.getProperty("server.port", "unknown"));
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
            host = "unknown";
        }

        return new TouristResponse(
                environment.getProperty("spring.application.name", "TOURIST-SERVICE"),
                instanceId,
                host,
                port,
                "Tourist service response"
        );
    }
}
