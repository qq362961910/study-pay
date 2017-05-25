package com.jy.pay.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
@RequestMapping("/test")
@RestController
public class TestController {

    private final Logger logger = LogManager.getLogger(TestController.class.getName());
    @RequestMapping("/json")
    public Object testJson() {

        logger.trace("this is a trace message");
        logger.debug("this is a debug message");
        logger.info("this is a info message");
        logger.warn("this is a warn message");
        logger.error("this is a error message");

        return new HashMap<String, Object>() {{
            put("success", true);
        }};

    }
}
