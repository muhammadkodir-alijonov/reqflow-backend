package com.reqflow.analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlAnalysisResponseDto {
        private String url;
        private String serverIp;
        private int statusCode;
        private String protocol;
        private long totalTimeMs;
        private TimingBreakdownDto timings;
}
