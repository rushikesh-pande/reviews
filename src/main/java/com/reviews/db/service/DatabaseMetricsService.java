package com.reviews.db.service;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Database Optimisation Enhancement: Database Metrics Service
 *
 * Tracks cache and query performance metrics for reviews.
 * Exposed to Prometheus via /actuator/prometheus.
 *
 * Metrics:
 *  - reviews_cache_hits_total       — Redis cache hits
 *  - reviews_cache_misses_total     — Redis cache misses (DB queries)
 *  - reviews_db_queries_total       — Total DB queries by type
 *  - reviews_db_slow_queries_total  — Queries above 500ms
 *  - reviews_connection_pool_active — HikariCP active connections
 */
@Service
public class DatabaseMetricsService {

    private final MeterRegistry meterRegistry;
    private final AtomicLong activeConnections = new AtomicLong(0);

    public DatabaseMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        Gauge.builder("reviews.connection.pool.active", activeConnections, AtomicLong::get)
             .description("Active HikariCP connections for reviews")
             .tag("service", "reviews")
             .register(meterRegistry);
    }

    public void recordCacheHit(String cacheName) {
        Counter.builder("reviews.cache.hits.total")
               .tag("service", "reviews").tag("cache", cacheName)
               .description("Redis cache hits for reviews")
               .register(meterRegistry).increment();
    }

    public void recordCacheMiss(String cacheName) {
        Counter.builder("reviews.cache.misses.total")
               .tag("service", "reviews").tag("cache", cacheName)
               .description("Redis cache misses for reviews (DB fallback)")
               .register(meterRegistry).increment();
    }

    public void recordDbQuery(String queryType) {
        Counter.builder("reviews.db.queries.total")
               .tag("service", "reviews").tag("type", queryType)
               .description("DB queries for reviews")
               .register(meterRegistry).increment();
    }

    public void recordSlowQuery(String queryType, long ms) {
        Counter.builder("reviews.db.slow.queries.total")
               .tag("service", "reviews").tag("type", queryType)
               .description("DB queries exceeding 500ms for reviews")
               .register(meterRegistry).increment();
        meterRegistry.summary("reviews.db.query.duration",
                "service", "reviews", "type", queryType).record(ms);
    }

    public void setActiveConnections(long count) {
        activeConnections.set(count);
    }
}
