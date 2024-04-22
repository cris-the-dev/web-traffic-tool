package com.tiennln.webtraffictool.controllers;

import com.tiennln.webtraffictool.handlers.ProcessHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/process")
@AllArgsConstructor
public class ProcessController {

    private ProcessHandler handler;

    @PostMapping
    void process() {
        handler.startWithThreadPool();
    }

    @PostMapping("/terminate")
    void terminate() {
        handler.terminateThreadPool();
    }

    @GetMapping("/processed-thread")
    Integer getProcessedThread() {
        return handler.getProcessedThread();
    }
}
