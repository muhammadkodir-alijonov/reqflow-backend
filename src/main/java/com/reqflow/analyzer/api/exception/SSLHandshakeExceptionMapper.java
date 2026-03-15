package com.reqflow.analyzer.api.exception;

import java.time.Instant;

import javax.net.ssl.SSLHandshakeException;

import com.reqflow.analyzer.dto.ErrorResponseDto;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SSLHandshakeExceptionMapper implements ExceptionMapper<SSLHandshakeException> {

    @Override
    public Response toResponse(SSLHandshakeException exception) {
        ErrorResponseDto response = new ErrorResponseDto(
                "TLS_HANDSHAKE_FAILED",
                "TLS handshake failed. Verify certificate validity and TLS compatibility (TLS 1.3 recommended).",
                Instant.now()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
