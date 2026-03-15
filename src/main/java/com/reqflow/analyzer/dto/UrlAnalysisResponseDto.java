package com.reqflow.analyzer.dto;

public record UrlAnalysisResponseDto(
        String url,
        String serverIp,
        int statusCode,
        String protocol,
        long totalTimeMs,
        TimingBreakdownDto timings
) {
}
