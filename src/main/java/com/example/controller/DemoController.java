package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/demo")
    public String demo() {
        logger.info("Processing demo request");
        // Do some processing
        logger.info("Completed demo request");
        return "Demo completed";
    }
}
