package com.reqflow.analyzer.api.exception;

import java.net.SocketTimeoutException;
import java.time.Instant;

import com.reqflow.analyzer.dto.ErrorResponseDto;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SocketTimeoutExceptionMapper implements ExceptionMapper<SocketTimeoutException> {

    @Override
    public Response toResponse(SocketTimeoutException exception) {
        ErrorResponseDto response = new ErrorResponseDto(
                "REQUEST_TIMEOUT",
                "Connection or response timed out while reaching the target server.",
                Instant.now()
        );

        return Response.status(Response.Status.GATEWAY_TIMEOUT)
                .entity(response)
                .build();
    }
}
