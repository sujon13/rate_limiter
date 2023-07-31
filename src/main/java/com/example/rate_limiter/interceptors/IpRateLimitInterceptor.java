package com.example.rate_limiter.interceptors;

import com.example.rate_limiter.Util;
import com.example.rate_limiter.service.RateLimiter;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class IpRateLimitInterceptor implements HandlerInterceptor {
    private final RateLimiter rateLimiter;
    private Bucket bucket;


    public void initBucket(String key) {
        this.bucket = rateLimiter.resolveBucket(key);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = Util.getClientIpAddress(request);
        log.info("Client ip address: {}", ip);
        initBucket(ip);


        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            String errorMessage = "You have exhausted your API Request Quota";
            log.error("error messages: " + errorMessage);

            Util.buildErrorResponse(response, errorMessage);
            return false;
        }
     }

}
