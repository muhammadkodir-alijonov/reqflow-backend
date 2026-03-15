package com.reqflow.analyzer.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import com.reqflow.analyzer.dto.UrlAnalysisResponseDto;
import com.reqflow.analyzer.service.UrlAnalysisService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api/analyze")
@Produces(MediaType.APPLICATION_JSON)
public class UrlAnalysisController {

    private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");

    @Inject
    UrlAnalysisService analysisService;

    @GET
    public UrlAnalysisResponseDto analyze(@QueryParam("url") String url) throws IOException {
        validateUrl(url);
        return analysisService.analyze(url);
    }

    private void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Query parameter 'url' is required.");
        }

        try {
            URI parsed = new URI(url);
            if (parsed.getHost() == null || parsed.getScheme() == null) {
                throw new IllegalArgumentException("URL must include a valid scheme and host.");
            }

            if (!ALLOWED_SCHEMES.contains(parsed.getScheme().toLowerCase())) {
                throw new IllegalArgumentException("Only http and https URLs are supported.");
            }
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URL format: " + ex.getInput(), ex);
        }
    }
}
