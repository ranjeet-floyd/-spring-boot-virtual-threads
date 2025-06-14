package com.example.controller;

import com.example.service.BlockingOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller demonstrating Virtual Thread capabilities in Spring Boot.
 * Provides endpoints to test and compare virtual thread performance with different
 * concurrent operation scenarios.
 *
 * Endpoints:
 * - /api/blocking/{delayMs}: Demonstrates simple blocking operations
 * - /api/parallel/{count}/{delayMs}: Shows parallel processing with virtual threads
 * - /api/thread-info: Provides information about the current thread
 *
 * Each endpoint is designed to showcase different aspects of virtual thread behavior
 * and performance characteristics.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
public class VirtualThreadController {
    private static final Logger logger = LoggerFactory.getLogger(VirtualThreadController.class);
    private final BlockingOperationService blockingOperationService;

    public VirtualThreadController(BlockingOperationService blockingOperationService) {
        this.blockingOperationService = blockingOperationService;
    }

    @GetMapping("/blocking/{delayMs}")
    public String blockingEndpoint(@PathVariable long delayMs) {
        logger.info("Received request on thread: {}", Thread.currentThread());
        return blockingOperationService.simulateBlockingOperation(delayMs);
    }

    @GetMapping("/parallel/{count}/{delayMs}")
    public List<String> parallelOperations(@PathVariable int count, @PathVariable long delayMs) {
        logger.info("Starting parallel operations on thread: {}", Thread.currentThread());
        List<String> results = new ArrayList<>();
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> futures = new ArrayList<>();
            
            for (int i = 0; i < count; i++) {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(
                    () -> blockingOperationService.simulateIOOperation(delayMs),
                    executor
                );
                futures.add(future);
            }
            
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
            for (CompletableFuture<String> future : futures) {
                results.add(future.join());
            }
        }
        
        logger.info("Completed parallel operations on thread: {}", Thread.currentThread());
        return results;
    }

    @GetMapping("/thread-info")
    public CompletableFuture<String> getThreadInfo() {
        return CompletableFuture.supplyAsync(() -> {
            Thread currentThread = Thread.currentThread();
            return String.format("Thread Info - Name: %s, Type: %s, Is Virtual: %s",
                currentThread.getName(),
                currentThread.getClass().getName(),
                currentThread.isVirtual()
            );
        }, Executors.newVirtualThreadPerTaskExecutor());
    }
}
