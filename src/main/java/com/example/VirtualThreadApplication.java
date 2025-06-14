package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;

/**
 * Main Spring Boot application class demonstrating Virtual Thread capabilities.
 * This application showcases the use of Virtual Threads (Project Loom) in a Spring Boot
 * environment, providing improved scalability for I/O-bound operations.
 *
 * Key features:
 * - Virtual thread configuration for Tomcat connector
 * - Scheduled thread monitoring
 * - REST endpoints demonstrating virtual thread usage
 *
 * @see com.example.controller.VirtualThreadController
 * @see com.example.monitoring.ThreadMonitor
 */
@SpringBootApplication
@EnableScheduling
public class VirtualThreadApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualThreadApplication.class, args);
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

    @Bean
    public org.springframework.core.task.AsyncTaskExecutor applicationTaskExecutor() {
        return new org.springframework.core.task.VirtualThreadTaskExecutor();
    }
}
