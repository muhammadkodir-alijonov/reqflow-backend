package com.reqflow.analyzer.controller;

import com.reqflow.analyzer.dto.VisitorCountResponseDto;
import com.reqflow.analyzer.service.VisitorPresenceService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/visitors")
@Produces(MediaType.APPLICATION_JSON)
public class VisitorController {

    @Inject
    VisitorPresenceService visitorPresenceService;

    @POST
    @Path("/hit")
    public VisitorCountResponseDto hit() {
        long totalVisits = visitorPresenceService.registerVisit();
        return buildResponse(totalVisits);
    }

    @GET
    @Path("/total")
    public VisitorCountResponseDto total() {
        return buildResponse(visitorPresenceService.currentTotal());
    }

    @GET
    @Path("/ping")
    public VisitorCountResponseDto ping() {
        return total();
    }

    @GET
    @Path("/current")
    public VisitorCountResponseDto current() {
        return total();
    }

    private VisitorCountResponseDto buildResponse(long totalVisits) {
        return VisitorCountResponseDto.builder()
                .totalVisits(totalVisits)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
