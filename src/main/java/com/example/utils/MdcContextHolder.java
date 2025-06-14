package com.example.utils;

import org.slf4j.MDC;
import java.util.Map;
import java.util.HashMap;

public class MdcContextHolder {
    public static final String REQUEST_ID = "requestId";
    public static final String USER_ID = "userId";
    public static final String SESSION_ID = "sessionId";
    public static final String MODULE = "module";
    public static final String ENVIRONMENT = "env";

    public static void setMdcContext(Map<String, String> context) {
        context.forEach(MDC::put);
    }

    public static Map<String, String> getMdcContext() {
        Map<String, String> context = new HashMap<>();
        context.put(REQUEST_ID, MDC.get(REQUEST_ID));
        context.put(USER_ID, MDC.get(USER_ID));
        context.put(SESSION_ID, MDC.get(SESSION_ID));
        context.put(MODULE, MDC.get(MODULE));
        context.put(ENVIRONMENT, MDC.get(ENVIRONMENT));
        return context;
    }

    public static void clearMdcContext() {
        MDC.clear();
    }
}
