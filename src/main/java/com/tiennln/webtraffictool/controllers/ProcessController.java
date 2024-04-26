package com.tiennln.webtraffictool.controllers;

import com.tiennln.webtraffictool.handlers.ProcessHandler;
import com.tiennln.webtraffictool.helpers.ThreadHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/process")
@AllArgsConstructor
@Slf4j
public class ProcessController {

    private ProcessHandler handler;

    @PostMapping
    void process() {
        handler.startWithThreadPool();
    }

    @PostMapping("/single")
    void processSingle(@RequestParam String port) {
        new Thread(() -> {
            do {
                try {
                    handler.start(port);
                    ThreadHelper.waitInMs(300 * 1000); // 300s
                } catch (Exception ex) {
                    log.error("Error while processing, {}", ex.getMessage());
                    ThreadHelper.waitInMs( 1000); // 1s
                }
            } while (true);
        }).start();
    }

    @PostMapping("/terminate")
    void terminate() {
        handler.terminateThreadPool();
    }

    @GetMapping("/processed-thread")
    Integer getProcessedThread() {
        return handler.getProcessedThread();
    }

    @GetMapping("/port-status")
    Map<String, Boolean> getPortStatus() {
        return handler.getPortStatus();
    }
}
