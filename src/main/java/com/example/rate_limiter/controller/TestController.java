package com.example.rate_limiter.controller;

import com.example.rate_limiter.model.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> test(HttpServletRequest request) {
        log.info("Api Request -  " + request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.ofSuccess("Congrats! You have successfully received response", "success"));
    }

}
