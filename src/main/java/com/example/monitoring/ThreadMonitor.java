package com.example.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ThreadMonitor {
    private static final Logger logger = LoggerFactory.getLogger(ThreadMonitor.class);
    private final AtomicInteger virtualThreadCounter = new AtomicInteger(0);
    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public void incrementVirtualThreadCount() {
        virtualThreadCounter.incrementAndGet();
    }

    public void decrementVirtualThreadCount() {
        virtualThreadCounter.decrementAndGet();
    }

    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    public void logThreadStatistics() {
        ThreadInfo[] threadInfo = threadMXBean.dumpAllThreads(false, false);
        long totalThreads = threadInfo.length;
        long daemonThreads = Arrays.stream(threadInfo)
                .filter(ThreadInfo::isDaemon)
                .count();

        logger.info("Thread Statistics:");
        logger.info("Total Threads: {}", totalThreads);
        logger.info("Platform Threads: {}", totalThreads - virtualThreadCounter.get());
        logger.info("Virtual Threads: {}", virtualThreadCounter.get());
        logger.info("Daemon Threads: {}", daemonThreads);
        logger.info("Peak Thread Count: {}", threadMXBean.getPeakThreadCount());
        
        // Log detailed thread state information
        Arrays.stream(Thread.State.values()).forEach(state -> {
            long count = Arrays.stream(threadInfo)
                    .filter(ti -> ti.getThreadState() == state)
                    .count();
            logger.debug("Threads in {} state: {}", state, count);
        });
    }
}
