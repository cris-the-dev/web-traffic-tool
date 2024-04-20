package com.tiennln.webtraffictool.helpers;

import lombok.extern.slf4j.Slf4j;

/**
 * The ThreadHelper is a class that ...
 *
 * @author Theon Tran Phan Truong Thinh
 * @version 1.0
 * @since 20/04/2024 1:56 PM
 */
@Slf4j
public class ThreadHelper {

    private ThreadHelper() {
    }

    public static void waitInMs(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException ex) {
            log.error("Failed to perform sleep");
            Thread.currentThread().interrupt();
        }
    }
}
