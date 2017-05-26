package com.jy.pay.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
@RequestMapping("/test")
@RestController
public class TestController {

    private final Logger logger = LogManager.getLogger(TestController.class);
    @RequestMapping("/json")
    public Object testJson(@RequestParam Map<String, Object> param) {

        logger.trace("param: " + param);
        logger.debug("param: " + param);
        logger.info("param: " + param);
        logger.warn("param: " + param);
        logger.error("param: " + param);

        return new HashMap<String, Object>() {{
            put("success", true);
        }};

    }
}
