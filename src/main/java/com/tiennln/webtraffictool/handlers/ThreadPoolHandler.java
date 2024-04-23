package com.tiennln.webtraffictool.handlers;

import com.tiennln.webtraffictool.configurations.AppConfig;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@ApplicationScope
@AllArgsConstructor
public class ThreadPoolHandler {

    private static ThreadPoolExecutor executor;
    public static boolean isTerminated = true;
    public static Integer totalThread = 0;

    private AppConfig appConfig;

    @PostConstruct
    private void initPool() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(appConfig.getPoolSize());
    }

    public void start(Runnable task) {
        if (executor.getQueue().size() < appConfig.getMaxQueueSize()) {
            totalThread += 1;
            executor.submit(task);
        }
    }

    public Integer getPoolSize() {
        return executor.getPoolSize();
    }

    public Integer getProcessingThread() {
        return executor.getActiveCount();
    }

    public Integer getWaitingThread() {
        return executor.getQueue().size();
    }

    public Integer getProcessedThread() {
        return totalThread;
    }
}
