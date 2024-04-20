package com.tiennln.webtraffictool.handlers;

import com.tiennln.webtraffictool.helpers.ThreadHelper;
import com.tiennln.webtraffictool.services.SeleniumService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@AllArgsConstructor
public class ProcessHandler {

    private SeleniumService seleniumService;

    public void start() {
        // Get driver
        var driver = seleniumService.getDriver();

        // Open base url
        seleniumService.openBrowser(driver, "https://blast1995.com");

        // Wait for page loaded
        var waitResult = seleniumService.waitForPageReady(driver, Duration.ofSeconds(5), 3);
        if (!waitResult) {
            return;
        }

        // Get base window
        var baseWindow = driver.getWindowHandle();

        // Click ads
        waitResult = seleniumService.clickByXPath(driver, "(/html/div)[last()]", Duration.ofSeconds(5), 3);
        if (!waitResult) {
            return;
        }

        // Wait for new tab opened
        seleniumService.waitNumberOfWindowsToBe(driver, 2, Duration.ofSeconds(5));

        // Switch to new window opened
        for (String winHandle : driver.getWindowHandles()) {
            if (!StringUtils.equals(baseWindow, winHandle)) {
                driver.switchTo().window(winHandle);
                break;
            }
        }

        // Wait for page loaded
        waitResult = seleniumService.waitForPageReady(driver, Duration.ofSeconds(5), 3);
        if (!waitResult) {
            return;
        }

        // Should wait to keep impression
        ThreadHelper.waitInMs(1000);

        // Close
        driver.quit();
    }
}
