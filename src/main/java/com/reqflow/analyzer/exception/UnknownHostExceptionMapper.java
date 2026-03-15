package com.reqflow.analyzer.exception;

import java.net.UnknownHostException;
import java.time.Instant;

import com.reqflow.analyzer.dto.ErrorResponseDto;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnknownHostExceptionMapper implements ExceptionMapper<UnknownHostException> {

    @Override
    public Response toResponse(UnknownHostException exception) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .error("DNS_RESOLUTION_FAILED")
                .message("Could not resolve host name. Check the URL domain.")
                .timestamp(Instant.now())
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
