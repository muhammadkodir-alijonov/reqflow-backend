package com.reqflow.analyzer.exception;

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
        ErrorResponseDto response = ErrorResponseDto.builder()
                .error("TLS_HANDSHAKE_FAILED")
                .message("TLS handshake failed. Verify certificate validity and TLS compatibility (TLS 1.3 recommended).")
                .timestamp(Instant.now())
                .build();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
