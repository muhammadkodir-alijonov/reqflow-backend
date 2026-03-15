package com.reqflow.analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimingBreakdownDto {
        private long dnsLookupMs;
        private long tcpHandshakeMs;
        private long tlsHandshakeMs;
        private long httpRequestMs;
        private long httpResponseMs;
}
