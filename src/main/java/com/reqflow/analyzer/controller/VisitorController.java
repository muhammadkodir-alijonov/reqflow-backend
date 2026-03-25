package com.reqflow.analyzer.controller;

import com.reqflow.analyzer.dto.VisitorCountResponseDto;
import com.reqflow.analyzer.service.VisitorPresenceService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api/visitors")
@Produces(MediaType.APPLICATION_JSON)
public class VisitorController {

    @Inject
    VisitorPresenceService visitorPresenceService;

    @GET
    @Path("/ping")
    public VisitorCountResponseDto ping(@QueryParam("clientId") String clientId) {
        validateClientId(clientId);
        int activeVisitors = visitorPresenceService.touch(clientId);
        return VisitorCountResponseDto.builder()
                .activeVisitors(activeVisitors)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @GET
    @Path("/current")
    public VisitorCountResponseDto current() {
        int activeVisitors = visitorPresenceService.currentCount();
        return VisitorCountResponseDto.builder()
                .activeVisitors(activeVisitors)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    private void validateClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Query parameter 'clientId' is required.");
        }

        if (clientId.length() > 128) {
            throw new IllegalArgumentException("Query parameter 'clientId' exceeds max length.");
        }
    }
}
