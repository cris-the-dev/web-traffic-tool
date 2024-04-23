package com.tiennln.webtraffictool.handlers;

import com.tiennln.webtraffictool.helpers.ThreadHelper;
import com.tiennln.webtraffictool.services.GeoNodeService;
import com.tiennln.webtraffictool.services.SeleniumService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;

@Component
@AllArgsConstructor
@Slf4j
public class ProcessHandler {

    private SeleniumService seleniumService;

    private ThreadPoolHandler threadPoolHandler;

    private static final String SEARCH_URL = "https://google.com";

    private static final String TARGET_URL = "https://blast1995.com/";

    public void start() {
        log.info("--- Start new process");
        // Get driver
        var driver = seleniumService.getDriver();

        ThreadHelper.setTimeout(() -> {
            log.warn("--- Ending by timeout");
            driver.quit();
        }, 210L * 1000); // 210s

        // Open base url
        seleniumService.openBrowser(driver, SEARCH_URL);

        // Get base window
        var baseWindow = driver.getWindowHandle();

        // Wait for page loaded
        seleniumService.waitForPageReady(driver, Duration.ofSeconds(15), 2, () -> driver.get(SEARCH_URL));

        // Do search
        doSearchByGoogle(driver);

        // Should wait to keep impression
        ThreadHelper.waitInMs(2000);

        // Click ads
        clickTheFirstAds(driver, baseWindow);

        // Switch to base window
        driver.close();
        driver.switchTo().window(baseWindow);

        // Check if any remaining ads -> click
        clickRemainingAds(driver, baseWindow);

        // Click allow notification if any
        clickAllowNotification(driver, baseWindow);

        // Switch to base window
        driver.switchTo().window(baseWindow);

        ThreadHelper.waitInMs(5000);

        log.info("--- Ending happy");

        // Close
        driver.quit();
    }

    private void doSearchByGoogle(WebDriver driver) {
        log.info("Start doSearchByGoogle");

        if (driver.getCurrentUrl().startsWith("https://www.google.com/sorry/index")) {
            driver.get(TARGET_URL);
            return;
        }

        seleniumService.clickByXPath(driver, "//button[contains(., 'Avvis alle')]", Duration.ofSeconds(2), 0);
        seleniumService.clickByXPath(driver, "/html/body/div[2]/div[3]/div[3]/span/div/div/div/div[3]/div[1]/button[2]/div", Duration.ofSeconds(2), 0);

        var result = seleniumService.search(driver, "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/textarea", Duration.ofSeconds(2), "blast1995");
        if (!result) {
            driver.get(TARGET_URL);
            return;
        }
        result = seleniumService.waitForPageReady(driver, Duration.ofSeconds(15), 1, () -> driver.get(TARGET_URL));
        if (result && driver.getCurrentUrl().contains("google")) {
            result = seleniumService.clickByXPath(driver, "//*[@href='" + TARGET_URL + "']", Duration.ofMillis(1000), 2);
            if (result) {
                result = seleniumService.waitForPageReady(driver, Duration.ofSeconds(10), 1, () -> driver.navigate().refresh());
            }
            if (!result) {
                driver.get(TARGET_URL);
                seleniumService.waitForPageReady(driver, Duration.ofSeconds(20), 1, () -> driver.navigate().refresh());
            }
        }
        log.info("End doSearchByGoogle");
    }

    private void clickTheFirstAds(WebDriver driver, String baseWindow) {
        log.info("Start clickTheFirstAds");
        seleniumService.clickByXPath(driver, "(/html/div)[last()]", Duration.ofSeconds(10), 2);

        var numOfHandles = driver.getWindowHandles().size();

        while (numOfHandles == 1) {
            ThreadHelper.waitInMs(1000);
            numOfHandles = driver.getWindowHandles().size();
        }

        for (var handle : driver.getWindowHandles()) {
            if (!StringUtils.equals(handle, baseWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        boolean execResult = seleniumService.waitForPageReady(driver, Duration.ofSeconds(10), 2, () -> driver.navigate().refresh());

        if (execResult) {
            // Should wait to keep impression
            ThreadHelper.waitInMs(2000);
        }
        log.info("End clickTheFirstAds");
    }

    private void clickRemainingAds(WebDriver driver, String baseWindow) {
        log.info("Start clickRemainingAds");
        boolean execResult;
        do {
            execResult = seleniumService.clickByXPath(driver, "(/html/div)[last()]", Duration.ofSeconds(1), 0);
            if (execResult) {
                if (driver.getWindowHandles().size() > 1) {
                    driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));
                    driver.close();
                }
                driver.switchTo().window(baseWindow);
                ThreadHelper.waitInMs(1000);
            }
        } while (execResult);
        log.info("End clickRemainingAds");
    }

    private void clickAllowNotification(WebDriver driver, String baseWindow) {
        log.info("Start clickAllowNotification");
        var iFrames = driver.findElements(By.tagName("iframe"));
        var iFrameSize = iFrames.size();
        while (iFrameSize >= 1) {
            log.info("Found iFrame(s) size: {}", iFrameSize);
            try {
                var execNotiResult = false;
                for (var i = 0; i < iFrameSize; i++) {
                    driver.switchTo().window(baseWindow);
                    driver.switchTo().frame(iFrames.get(i));
                    execNotiResult = seleniumService.clickByXPath(driver, "//button[@id='B2']", Duration.ofSeconds(2), 0);
                    if (execNotiResult) {
                        break;
                    }
                }

                if (!execNotiResult) {
                    for (var i = 0; i < iFrameSize; i++) {
                        driver.switchTo().window(baseWindow);
                        driver.switchTo().frame(iFrames.get(i));
                        seleniumService.clickByXPath(driver, "//div[last()]", Duration.ofSeconds(2), 0);
                    }
                } else {
                    ThreadHelper.waitInMs(10000);
                    driver.switchTo().window(baseWindow);
                    if (driver.getWindowHandles().size() > 1) {
                        driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));
                    }
                    var execResult = seleniumService.waitForPageReady(driver, Duration.ofSeconds(10), 2, () -> driver.navigate().refresh());
                    if (execResult) {
                        // Should wait to keep impression
                        ThreadHelper.waitInMs(5000);
                    }
                    break;
                }
                ThreadHelper.waitInMs(5000);
                if (driver.getWindowHandles().size() > 1) {
                    driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));
                    driver.close();
                }
            } catch (Exception ex) {
                log.error("Error in clickAllowNotification", ex);
            }
            // Switch to base window
            driver.switchTo().window(baseWindow);
            iFrames = driver.findElements(By.tagName("iframe"));
            iFrameSize = iFrames.size();
        }
        log.info("End clickAllowNotification");
    }

    @Async
    public void startWithThreadPool() {
        ThreadPoolHandler.isTerminated = false;
        do {
            threadPoolHandler.start(() -> {
                log.info("Total pool size: " + threadPoolHandler.getPoolSize());
                log.info("Processing thread: " + threadPoolHandler.getProcessingThread());
                log.info("Waiting thread: " + threadPoolHandler.getWaitingThread());
                threadPoolHandler.start(this::start);
            });
        } while (!ThreadPoolHandler.isTerminated);
    }

    public void terminateThreadPool() {
        ThreadPoolHandler.isTerminated = true;
    }

    public Integer getProcessedThread() {
        return threadPoolHandler.getProcessedThread();
    }
}
