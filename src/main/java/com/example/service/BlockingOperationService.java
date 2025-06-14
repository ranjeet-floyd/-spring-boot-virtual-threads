package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlockingOperationService {
    private static final Logger logger = LoggerFactory.getLogger(BlockingOperationService.class);

    public String simulateBlockingOperation(long delayMillis) {
        try {
            logger.info("Starting blocking operation on thread: {}", Thread.currentThread());
            Thread.sleep(delayMillis);
            logger.info("Completed blocking operation on thread: {}", Thread.currentThread());
            return "Operation completed after " + delayMillis + "ms on thread: " + Thread.currentThread();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation interrupted", e);
        }
    }

    public String simulateIOOperation(long delayMillis) {
        try {
            logger.info("Starting IO operation on thread: {}", Thread.currentThread());
            // Simulate IO operation
            Thread.sleep(delayMillis);
            logger.info("Completed IO operation on thread: {}", Thread.currentThread());
            return "IO Operation completed after " + delayMillis + "ms on thread: " + Thread.currentThread();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation interrupted", e);
        }
    }
}
