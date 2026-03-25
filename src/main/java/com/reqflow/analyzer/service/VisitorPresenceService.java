package com.reqflow.analyzer.service;

import java.util.concurrent.atomic.AtomicLong;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VisitorPresenceService {

    private final AtomicLong totalVisits = new AtomicLong(0);

    public long registerVisit() {
        return totalVisits.incrementAndGet();
    }

    public long currentTotal() {
        return totalVisits.get();
    }
}
