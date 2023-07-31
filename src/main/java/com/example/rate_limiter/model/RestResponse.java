package com.example.rate_limiter.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestResponse<T> {
    private Boolean success;
    private String message;
    @ToString.Exclude
    private T payload;

    public static <T> RestResponse<T> of(boolean success, T payload, String msg) {
        return success ? ofSuccess(payload, msg) : ofError(payload, msg);
    }

    public static <T> RestResponse<T> of(boolean success, T payload) {
        return success ? ofSuccess(payload) : ofError(payload);
    }

    public static <T> RestResponse<T> of(boolean success, String msg) {
        return success ? ofSuccess(msg) : ofError(msg);
    }

    public static <T> RestResponse<T> of(boolean success) {
        return success ? ofSuccess() : ofError();
    }

    public static <T> RestResponse<T> ofError() {
        return ofError(null, "error");
    }

    public static <T> RestResponse<T> ofError(String message) {
        return ofError(null, message);
    }

    public static <T> RestResponse<T> ofError(T payload) {
        return ofError(payload, "error");
    }

    public static <T> RestResponse<T> ofError(T payload, String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setPayload(payload);
        return response;
    }

    public static <T> RestResponse<T> ofSuccess(T payload) {
        return ofSuccess(payload, "success");
    }

    public static <T> RestResponse<T> ofSuccess() {
        return ofSuccess(null, "success");
    }

    public static <T> RestResponse<T> ofSuccess(String msg) {
        return ofSuccess(null, msg);
    }

    public static <T> RestResponse<T> ofSuccess(T payload, String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setPayload(payload);
        return response;
    }
}