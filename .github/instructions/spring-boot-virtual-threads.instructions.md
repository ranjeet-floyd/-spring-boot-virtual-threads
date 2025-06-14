Create a Spring Boot project demonstrating Virtual Thread capabilities with the following specifications:

1. Project Structure:
- Project name: spring-boot-virtual-threads
- Java version: 21
- Spring Boot version: 3.2.0
- Build tool: Maven
- Package name: com.example

2. Dependencies:
- spring-boot-starter-web
- spring-boot-starter-aop
- lombok

3. Main Components:

a) Main Application Class (VirtualThreadApplication.java):
- Enable virtual threads for Tomcat
- Configure async support
- Enable scheduling for monitoring

b) Service Layer (BlockingOperationService.java):
- Simulate blocking operations
- Simulate IO operations
- Include logging for thread information

c) Controller Layer (VirtualThreadController.java):
- Endpoint: GET /api/blocking/{delayMs}
- Endpoint: GET /api/parallel/{count}/{delayMs}
- Endpoint: GET /api/thread-info
- Use virtual threads for async operations

d) Monitoring (ThreadMonitor.java):
- Track virtual thread count
- Log thread statistics every 5 seconds
- Monitor thread states and peak counts

e) Aspect (ThreadMonitoringAspect.java):
- Monitor virtual thread lifecycle
- Log thread creation and completion
- Track active virtual threads

4. Configuration Files:

a) application.properties:
- Server port: 8080
- Enable virtual threads
- Configure logging levels
- Set Tomcat thread pool settings
- Configure async timeout

b) logback.xml:
- Console logging
- File logging for thread monitoring
- Daily rolling logs
- Async logging configuration

5. Test Cases:
- Virtual thread info verification
- Parallel operations testing
- Performance comparison tests
- Async operation testing

6. Key Features to Implement:
- Virtual thread per request handling
- Parallel task execution
- Thread monitoring and statistics
- Async request processing
- Performance comparison utilities

The project should demonstrate the benefits of virtual threads over platform threads, particularly in I/O-bound operations, and include comprehensive logging and monitoring capabilities.

Note: Make sure the system has Java 21 installed and configured properly before creating the project.