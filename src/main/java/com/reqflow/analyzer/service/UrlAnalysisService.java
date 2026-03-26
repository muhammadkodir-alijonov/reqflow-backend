package com.reqflow.analyzer.service;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.reqflow.analyzer.dto.TimingBreakdownDto;
import com.reqflow.analyzer.dto.UrlAnalysisResponseDto;
import com.reqflow.analyzer.net.HttpLifecycleEventListener;
import com.reqflow.analyzer.net.RequestTimingContext;

import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

@ApplicationScoped
public class UrlAnalysisService {

    private final OkHttpClient client;
    private final OkHttpClient coldClient;

    public UrlAnalysisService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(10))
                .writeTimeout(Duration.ofSeconds(10))
                .callTimeout(Duration.ofSeconds(10))
                .eventListenerFactory(call -> {
                    RequestTimingContext timingContext = call.request().tag(RequestTimingContext.class);
                    if (timingContext == null) {
                        timingContext = new RequestTimingContext();
                    }
                    return new HttpLifecycleEventListener(timingContext);
                })
                .build();

        // Short-lived pool + keepAlive=0 significantly reduces connection reuse for cold measurements.
        this.coldClient = this.client.newBuilder()
                .connectionPool(new ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
                .retryOnConnectionFailure(false)
                .build();
    }

    public UrlAnalysisResponseDto analyze(String url) throws IOException {
        return analyze(url, false);
    }

    public UrlAnalysisResponseDto analyze(String url, boolean cold) throws IOException {
        RequestTimingContext timingContext = new RequestTimingContext();

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get()
                .tag(RequestTimingContext.class, timingContext);

        if (cold) {
            requestBuilder
                    .header("Cache-Control", "no-cache, no-store, max-age=0")
                    .header("Pragma", "no-cache")
                    .header("Connection", "close");
        }

        Request request = requestBuilder.build();
        OkHttpClient selectedClient = cold ? coldClient : client;

        try (Response response = selectedClient.newCall(request).execute()) {
            timingContext.setStatusCode(response.code());
            timingContext.setProtocol(toDisplayProtocol(response.protocol()));

            // Drain body so responseBodyEnd callback is triggered reliably.
            drainResponseBody(response.body());
        }

        TimingBreakdownDto timingBreakdown = TimingBreakdownDto.builder()
            .dnsLookupMs(timingContext.getDnsLookupMs())
            .tcpHandshakeMs(timingContext.getTcpHandshakeMs())
            .tlsHandshakeMs(timingContext.getTlsHandshakeMs())
            .httpRequestMs(timingContext.getHttpRequestMs())
            .httpResponseMs(timingContext.getHttpResponseMs())
            .build();

        return UrlAnalysisResponseDto.builder()
            .url(url)
            .serverIp(timingContext.getServerIp())
            .statusCode(timingContext.getStatusCode())
            .protocol(timingContext.getProtocol())
            .totalTimeMs(timingContext.getTotalTimeMs())
            .timings(timingBreakdown)
            .build();
    }

    private void drainResponseBody(ResponseBody body) throws IOException {
        if (body == null) {
            return;
        }

        try (BufferedSource source = body.source()) {
            Buffer buffer = new Buffer();
            while (source.read(buffer, 8192) != -1L) {
                buffer.clear();
            }
        }
    }

    private String toDisplayProtocol(Protocol protocol) {
        if (protocol == null) {
            return "UNKNOWN";
        }

        return switch (protocol) {
            case HTTP_1_0 -> "HTTP/1.0";
            case HTTP_1_1 -> "HTTP/1.1";
            case HTTP_2 -> "HTTP/2";
            case H2_PRIOR_KNOWLEDGE -> "H2C";
            case QUIC -> "QUIC";
            case SPDY_3 -> "SPDY/3";
        };
    }
}
