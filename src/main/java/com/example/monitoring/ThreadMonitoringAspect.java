package com.example.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ThreadMonitoringAspect {
    private static final Logger logger = LoggerFactory.getLogger(ThreadMonitoringAspect.class);
    private final ThreadMonitor threadMonitor;

    public ThreadMonitoringAspect(ThreadMonitor threadMonitor) {
        this.threadMonitor = threadMonitor;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object monitorThread(ProceedingJoinPoint joinPoint) throws Throwable {
        Thread currentThread = Thread.currentThread();
        if (currentThread.isVirtual()) {
            threadMonitor.incrementVirtualThreadCount();
            logger.debug("Virtual thread started: {}", currentThread);
            
            try {
                return joinPoint.proceed();
            } finally {
                threadMonitor.decrementVirtualThreadCount();
                logger.debug("Virtual thread completed: {}", currentThread);
            }
        }
        
        return joinPoint.proceed();
    }
}
