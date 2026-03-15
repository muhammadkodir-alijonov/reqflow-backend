package com.reqflow.analyzer.dto;

import java.time.Instant;

public record ErrorResponseDto(
        String error,
        String message,
        Instant timestamp
) {
}
