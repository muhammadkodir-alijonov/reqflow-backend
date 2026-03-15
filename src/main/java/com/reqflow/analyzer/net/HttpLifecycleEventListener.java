package com.reqflow.analyzer.net;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;

public class HttpLifecycleEventListener extends EventListener {

    private final RequestTimingContext timingContext;

    public HttpLifecycleEventListener(RequestTimingContext timingContext) {
        this.timingContext = timingContext;
    }

    @Override
    public void callStart(Call call) {
        timingContext.markCallStart(System.nanoTime());
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        timingContext.markDnsStart(System.nanoTime());
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<java.net.InetAddress> inetAddressList) {
        timingContext.markDnsEnd(System.nanoTime());
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        timingContext.markConnectStart(System.nanoTime());
    }

    @Override
    public void secureConnectStart(Call call) {
        timingContext.markSecureConnectStart(System.nanoTime());
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        timingContext.markSecureConnectEnd(System.nanoTime());
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        timingContext.markConnectEnd(System.nanoTime());
        if (inetSocketAddress != null && inetSocketAddress.getAddress() != null) {
            timingContext.setServerIp(inetSocketAddress.getAddress().getHostAddress());
        }
    }

    @Override
    public void requestHeadersStart(Call call) {
        timingContext.markRequestHeadersStart(System.nanoTime());
    }

    @Override
    public void requestHeadersEnd(Call call, okhttp3.Request request) {
        timingContext.markRequestHeadersEnd(System.nanoTime());
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        timingContext.markRequestBodyEnd(System.nanoTime());
    }

    @Override
    public void responseHeadersStart(Call call) {
        timingContext.markResponseHeadersStart(System.nanoTime());
    }

    @Override
    public void responseHeadersEnd(Call call, okhttp3.Response response) {
        timingContext.markResponseHeadersEnd(System.nanoTime());
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        timingContext.markResponseBodyEnd(System.nanoTime());
    }

    @Override
    public void callEnd(Call call) {
        timingContext.markCallEnd(System.nanoTime());
    }

    @Override
    public void callFailed(Call call, java.io.IOException ioe) {
        timingContext.markCallEnd(System.nanoTime());
    }
}
