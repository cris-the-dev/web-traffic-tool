package com.tiennln.webtraffictool.handlers;

import com.tiennln.webtraffictool.services.SeleniumService;
import lombok.AllArgsConstructor;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProcessHandler {

    private SeleniumService seleniumService;

    public void start() {
        var driver = seleniumService.getDriver();
        var executor = (JavascriptExecutor) driver;

        seleniumService.openBrowser(driver, "https://blast1995.com");

//        seleniumService.scrollDown(executor);
//        seleniumService.scrollDown(executor);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        seleniumService.clickByXPath(driver, "/html/body/div/div/div/span");
    }
}
