package com.reqflow.analyzer.dto;

public record TimingBreakdownDto(
        long dnsLookupMs,
        long tcpHandshakeMs,
        long tlsHandshakeMs,
        long httpRequestMs,
        long httpResponseMs
) {
}
