package com.example.util;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadTestUtils {
    
    public static TestResult runWithThreadPool(int taskCount, long delayMs, boolean useVirtualThreads) {
        Instant start = Instant.now();
        List<String> results = new ArrayList<>();
        
        ThreadFactory threadFactory = useVirtualThreads ? 
            Thread.ofVirtual().factory() : 
            Thread.ofPlatform().factory();
            
        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(threadFactory)) {
            List<CompletableFuture<String>> futures = new ArrayList<>();
            
            // Submit tasks
            for (int i = 0; i < taskCount; i++) {
                final int taskId = i;
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(delayMs);
                        return String.format("Task %d completed on thread: %s", 
                            taskId, Thread.currentThread());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }, executor);
                futures.add(future);
            }
            
            // Wait for all tasks to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
            // Collect results
            for (CompletableFuture<String> future : futures) {
                results.add(future.join());
            }
        }
        
        Duration duration = Duration.between(start, Instant.now());
        return new TestResult(duration, results);
    }
    
    public record TestResult(Duration duration, List<String> results) {
        public long durationMs() {
            return duration.toMillis();
        }
        
        public int taskCount() {
            return results.size();
        }
        
        public double averageTaskTimeMs() {
            return (double) durationMs() / taskCount();
        }
    }
}
