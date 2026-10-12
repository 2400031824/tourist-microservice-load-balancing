package com.example.tourist;

public record TouristResponse(
        String service,
        String instanceId,
        String host,
        String port,
        String message
) {
}
