package com.zzq.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: Zhou Zhongqing
 * @date: 4/29/2025 9:50 PM
 */
@RestController
@RequestMapping("/api/index")
public class IndexController {
    private final Logger log = LoggerFactory.getLogger(IndexController.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(2000);

    @RequestMapping("/")
    public String index() {

        return "index";
    }
}
