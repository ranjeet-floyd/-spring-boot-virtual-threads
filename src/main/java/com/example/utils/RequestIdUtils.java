package com.example.utils;

import java.util.UUID;
import org.slf4j.MDC;

public class RequestIdUtils {
    public static final String REQUEST_ID_KEY = "requestId";

    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    public static void setRequestId(String requestId) {
        MDC.put(REQUEST_ID_KEY, requestId);
    }

    public static String getRequestId() {
        return MDC.get(REQUEST_ID_KEY);
    }

    public static void clearRequestId() {
        MDC.remove(REQUEST_ID_KEY);
    }
}
