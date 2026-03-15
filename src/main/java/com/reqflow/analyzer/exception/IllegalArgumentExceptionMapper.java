package com.reqflow.analyzer.exception;

import java.time.Instant;

import com.reqflow.analyzer.dto.ErrorResponseDto;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .error("INVALID_REQUEST")
                .message(exception.getMessage())
                .timestamp(Instant.now())
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
