package com.tiennln.webtraffictool.handlers;

import com.tiennln.webtraffictool.helpers.ThreadHelper;
import com.tiennln.webtraffictool.services.SeleniumService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@AllArgsConstructor
public class ProcessHandler {

    private SeleniumService seleniumService;

    public void start() {
        // Get driver
        var driver = seleniumService.getDriver();

         ThreadHelper.setTimeout(driver::quit, 90L * 1000); // 90s

        // Open base url
        seleniumService.openBrowser(driver, "https://blast1995.com");

        // Wait for page loaded
        var execResult = seleniumService.waitForPageReady(driver, Duration.ofSeconds(5), 3, () -> driver.get("https://blast1995.com"));
        if (!execResult) {
            return;
        }
        // Should wait to keep impression
        ThreadHelper.waitInMs(1000);

        // Get base window
        var baseWindow = driver.getWindowHandle();

        // Click ads
        seleniumService.clickByXPath(driver, "(/html/div)[last()]", Duration.ofSeconds(5), 3);

        var numOfHandles = driver.getWindowHandles().size();

        while (numOfHandles == 1) {
            ThreadHelper.waitInMs(500);
            numOfHandles = driver.getWindowHandles().size();
        }

        for (var handle : driver.getWindowHandles()) {
            if (!StringUtils.equals(handle, baseWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        execResult = seleniumService.waitForPageReady(driver, Duration.ofSeconds(5), 3, () -> driver.navigate().refresh());

        if (execResult) {
            // Should wait to keep impression
            ThreadHelper.waitInMs(1000);
        }

        // Switch to base window
        driver.close();
        driver.switchTo().window(baseWindow);

        // Check if any xPath
        do {
            execResult = seleniumService.clickByXPath(driver, "(/html/div)[last()]", Duration.ofMillis(500), 0);
            if (execResult) {
                if (driver.getWindowHandles().size() > 1) {
                    driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));
                    driver.close();
                }
                driver.switchTo().window(baseWindow);
                ThreadHelper.waitInMs(500);
            }
        } while (execResult);

        // Wait to iframe
        var iFrames = driver.findElements(By.tagName("iframe"));
        var iFrameSize = iFrames.size();
        while (iFrameSize >= 1) {
            driver.switchTo().frame(iFrames.get(0));
            var execNotiResult = seleniumService.clickByXPath(driver, "//button[@id='B2']", Duration.ofSeconds(1), 0);
            if (!execNotiResult) {
                driver.switchTo().window(baseWindow);
                driver.switchTo().frame(iFrames.get(iFrameSize - 1));
                seleniumService.clickByXPath(driver, "//div[last()]", Duration.ofSeconds(1), 0);
            } else {
                ThreadHelper.waitInMs(1000);
                driver.switchTo().window(baseWindow);
                if (driver.getWindowHandles().size() > 1) {
                    driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));
                }
                execResult = seleniumService.waitForPageReady(driver, Duration.ofSeconds(5), 3, () -> driver.navigate().refresh());
                if (execResult) {
                    // Should wait to keep impression
                    ThreadHelper.waitInMs(2000);
                }
                break;
            }
            ThreadHelper.waitInMs(500);
            if (driver.getWindowHandles().size() > 1) {
                driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));
                driver.close();
            }
            // Switch to base window
            driver.switchTo().window(baseWindow);
            iFrames = driver.findElements(By.tagName("iframe"));
            iFrameSize = iFrames.size();
        }

        // Switch to base window
        driver.switchTo().window(baseWindow);

        ThreadHelper.waitInMs(2000);

        // Close
        driver.quit();
    }
}
