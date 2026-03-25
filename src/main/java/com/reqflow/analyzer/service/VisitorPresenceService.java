package com.reqflow.analyzer.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VisitorPresenceService {

    private static final long VISITOR_TTL_MS = 30_000;
    private final Map<String, Long> activeVisitors = new ConcurrentHashMap<>();

    public int touch(String clientId) {
        long now = System.currentTimeMillis();
        activeVisitors.put(clientId, now);
        purgeExpired(now);
        return activeVisitors.size();
    }

    public int currentCount() {
        long now = System.currentTimeMillis();
        purgeExpired(now);
        return activeVisitors.size();
    }

    private void purgeExpired(long now) {
        activeVisitors.entrySet().removeIf((entry) -> now - entry.getValue() > VISITOR_TTL_MS);
    }
}
