package com.example;

import com.example.controller.VirtualThreadController;
import com.example.service.BlockingOperationService;
import com.example.util.ThreadTestUtils;
import com.example.util.ThreadTestUtils.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class VirtualThreadApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlockingOperationService blockingOperationService;

    @Test
    void contextLoads() {
        assertNotNull(blockingOperationService);
    }

    @Test
    void testThreadInfo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/thread-info"))
                .andExpect(request -> assertTrue(
                    request.getAsyncResult().toString().contains("Is Virtual: true"),
                    "Expected thread to be virtual, got: " + request.getAsyncResult()
                ))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Is Virtual: true")));
    }

    @ParameterizedTest
    @CsvSource({
        "100, 50",   // 100 tasks, 50ms delay
        "500, 20",   // 500 tasks, 20ms delay
        "1000, 10"   // 1000 tasks, 10ms delay
    })
    void compareVirtualVsPlatformThreads(int taskCount, long delayMs) {
        // Run with platform threads
        TestResult platformResult = ThreadTestUtils.runWithThreadPool(taskCount, delayMs, false);
        
        // Run with virtual threads
        TestResult virtualResult = ThreadTestUtils.runWithThreadPool(taskCount, delayMs, true);
        
        // Log results
        System.out.printf("""
            
            Thread Performance Comparison:
            Tasks: %d, Delay per task: %dms
            Platform Threads:
              Total Time: %dms
              Avg Time per Task: %.2fms
            Virtual Threads:
              Total Time: %dms
              Avg Time per Task: %.2fms
            Improvement: %.2fx
            
            """,
            taskCount, delayMs,
            platformResult.durationMs(), platformResult.averageTaskTimeMs(),
            virtualResult.durationMs(), virtualResult.averageTaskTimeMs(),
            (double) platformResult.durationMs() / virtualResult.durationMs()
        );
        
        // Verify results
        assertTrue(virtualResult.durationMs() < platformResult.durationMs(),
                "Virtual threads should be faster than platform threads");
        
        assertEquals(taskCount, platformResult.taskCount(),
                "Platform threads should complete all tasks");
        assertEquals(taskCount, virtualResult.taskCount(),
                "Virtual threads should complete all tasks");
    }

    @Test
    void testParallelEndpoint() throws Exception {
        int taskCount = 10;
        long delayMs = 100;
        
        mockMvc.perform(get("/api/parallel/{count}/{delayMs}", taskCount, delayMs))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(containsString("VirtualThread")));
    }
}
