package com.example.rate_limiter.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RateLimiter {
    @Value("${public_api.req_limit_per_min:2}")
    private long requestLimitPerMin;

    private final ProxyManager<String> proxyManager;

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplierForUser(key);

        // Does not always create a new bucket, but instead returns the existing one if it exists.
        return proxyManager.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String key) {
        Refill refill = Refill.intervally(requestLimitPerMin, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(requestLimitPerMin, refill);
        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }
}