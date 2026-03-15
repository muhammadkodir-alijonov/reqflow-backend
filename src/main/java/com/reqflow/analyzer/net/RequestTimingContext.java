package com.reqflow.analyzer.net;

import java.util.concurrent.TimeUnit;

public class RequestTimingContext {

    private long callStartNanos = -1L;
    private long callEndNanos = -1L;

    private long dnsStartNanos = -1L;
    private long dnsLookupMs;

    private long connectStartNanos = -1L;
    private long tcpHandshakeMs;

    private long secureConnectStartNanos = -1L;
    private long tlsHandshakeMs;

    private long requestStartNanos = -1L;
    private long requestEndNanos = -1L;

    private long responseStartNanos = -1L;
    private long responseEndNanos = -1L;

    private String serverIp;
    private int statusCode;
    private String protocol = "UNKNOWN";

    public void markCallStart(long nanos) {
        this.callStartNanos = nanos;
    }

    public void markCallEnd(long nanos) {
        this.callEndNanos = nanos;
    }

    public void markDnsStart(long nanos) {
        this.dnsStartNanos = nanos;
    }

    public void markDnsEnd(long nanos) {
        if (dnsStartNanos >= 0L) {
            dnsLookupMs += toMillis(nanos - dnsStartNanos);
            dnsStartNanos = -1L;
        }
    }

    public void markConnectStart(long nanos) {
        this.connectStartNanos = nanos;
    }

    public void markConnectEnd(long nanos) {
        if (connectStartNanos >= 0L) {
            tcpHandshakeMs += toMillis(nanos - connectStartNanos);
            connectStartNanos = -1L;
        }
    }

    public void markSecureConnectStart(long nanos) {
        this.secureConnectStartNanos = nanos;
    }

    public void markSecureConnectEnd(long nanos) {
        if (secureConnectStartNanos >= 0L) {
            tlsHandshakeMs += toMillis(nanos - secureConnectStartNanos);
            secureConnectStartNanos = -1L;
        }
    }

    public void markRequestHeadersStart(long nanos) {
        this.requestStartNanos = nanos;
    }

    public void markRequestHeadersEnd(long nanos) {
        // For requests without a body (GET/HEAD), body end callback may not fire.
        if (requestStartNanos >= 0L && requestEndNanos < 0L) {
            requestEndNanos = nanos;
        }
    }

    public void markRequestBodyEnd(long nanos) {
        if (requestStartNanos >= 0L) {
            requestEndNanos = nanos;
        }
    }

    public void markResponseHeadersStart(long nanos) {
        this.responseStartNanos = nanos;
    }

    public void markResponseHeadersEnd(long nanos) {
        // Fallback for responses without a body.
        if (responseStartNanos >= 0L && responseEndNanos < 0L) {
            responseEndNanos = nanos;
        }
    }

    public void markResponseBodyEnd(long nanos) {
        if (responseStartNanos >= 0L) {
            responseEndNanos = nanos;
        }
    }

    public long getDnsLookupMs() {
        return dnsLookupMs;
    }

    public long getTcpHandshakeMs() {
        return tcpHandshakeMs;
    }

    public long getTlsHandshakeMs() {
        return tlsHandshakeMs;
    }

    public long getHttpRequestMs() {
        return durationMillis(requestStartNanos, requestEndNanos);
    }

    public long getHttpResponseMs() {
        return durationMillis(responseStartNanos, responseEndNanos);
    }

    public long getTotalTimeMs() {
        return durationMillis(callStartNanos, callEndNanos);
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private long durationMillis(long start, long end) {
        if (start < 0L || end < 0L || end < start) {
            return 0L;
        }
        return toMillis(end - start);
    }

    private long toMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }
}
