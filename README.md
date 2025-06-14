# Spring Boot Virtual Threads Demo

This project demonstrates the use of Virtual Threads (Project Loom) in a Spring Boot application using Java 21. It showcases the benefits of virtual threads for handling concurrent operations and improving application performance.

## Features

- Virtual Thread implementation in Spring Boot 3.2
- REST endpoints demonstrating virtual thread usage
- Real-time thread monitoring and statistics
- Performance comparison between virtual and platform threads
- Comprehensive logging system
- Detailed test suite

## Prerequisites

- Java 21 or higher
- Maven 3.8+
- Spring Boot 3.2.0+

## Building the Application

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

1. **Simple Blocking Operation**
   ```
   GET /api/blocking/{delayMs}
   ```
   Simulates a blocking operation with specified delay.

2. **Parallel Operations**
   ```
   GET /api/parallel/{count}/{delayMs}
   ```
   Executes multiple operations in parallel using virtual threads.

3. **Thread Information**
   ```
   GET /api/thread-info
   ```
   Returns information about the current thread.

## Monitoring

The application includes comprehensive thread monitoring:

- Real-time console logging
- Thread statistics updated every 5 seconds
- Dedicated monitoring log file: `logs/thread-monitor.log`
- Thread state and performance metrics

## Testing

The project includes several types of tests:

1. **Performance Tests**
   ```bash
   mvn test -Dtest=VirtualThreadApplicationTests#compareVirtualVsPlatformThreads
   ```
   Compares virtual threads vs platform threads performance.

2. **Integration Tests**
   ```bash
   mvn test -Dtest=VirtualThreadApplicationTests#testParallelEndpoint
   ```
   Tests the parallel processing endpoint.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           ├── VirtualThreadApplication.java
│   │           ├── controller/
│   │           │   └── VirtualThreadController.java
│   │           ├── service/
│   │           │   └── BlockingOperationService.java
│   │           └── monitoring/
│   │               ├── ThreadMonitor.java
│   │               └── ThreadMonitoringAspect.java
│   └── resources/
│       ├── application.properties
│       └── logback.xml
└── test/
    └── java/
        └── com/
            └── example/
                ├── VirtualThreadApplicationTests.java
                └── util/
                    └── ThreadTestUtils.java
```

## Key Components

1. **VirtualThreadApplication**: Main application class with virtual thread configuration
2. **VirtualThreadController**: REST endpoints demonstrating virtual thread usage
3. **BlockingOperationService**: Service simulating blocking operations
4. **ThreadMonitor**: Monitors and reports thread statistics
5. **ThreadMonitoringAspect**: AOP aspect for thread lifecycle monitoring

## Performance Metrics

The application includes built-in performance testing that demonstrates:
- Throughput comparison between virtual and platform threads
- Scalability with increasing concurrent operations
- Resource utilization metrics

## Logging Configuration

The application uses a two-tier logging strategy:
1. Console logging for general application events
2. Dedicated file logging for thread monitoring

## Best Practices Demonstrated

1. **Virtual Thread Usage**
   - Per-task thread creation
   - Proper thread lifecycle management
   - Integration with Spring's async capabilities

2. **Monitoring**
   - Real-time statistics
   - Thread state tracking
   - Performance metrics

3. **Testing**
   - Comprehensive test coverage
   - Performance benchmarking
   - Integration testing

## Contributing

Feel free to submit issues and enhancement requests!
