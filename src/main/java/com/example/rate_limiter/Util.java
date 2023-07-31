package com.example.rate_limiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

public class Util {
    private Util() {
    }


    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getClientIpAddress(HttpServletRequest request) {
        return Stream.of(HEADERS_TO_TRY)
                .map(request::getHeader)
                .filter(Objects::nonNull)
                .filter(ip -> ip.length() != 0 && !"unknown".equalsIgnoreCase(ip))
                .findFirst()
                .orElse(request.getRemoteAddr());
    }

    public static void buildErrorResponse(HttpServletResponse response, String error) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write(error);
    }
}
