package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.helpers.ThreadHelper;
import com.tiennln.webtraffictool.services.ProxyService;
import com.tiennln.webtraffictool.services.SeleniumService;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
@Slf4j
public class SeleniumServiceImpl implements SeleniumService {

    private ProxyService proxyService;

    public WebDriver getDriver() {
        WebDriverManager.chromedriver().clearDriverCache().setup();

        var proxyServer = proxyService.getProxy();

        var proxy = new Proxy();
        proxy.setHttpProxy(proxyServer);
        proxy.setSslProxy(proxyServer);

        var options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.setCapability("browserName", "chrome");
        options.setBrowserVersion("117.0.0");
        options.setPlatformName("Mac OS X");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setCapability("proxy", proxy);

        return new ChromeDriver(options);
    }

    public void openBrowser(WebDriver driver, String url) {
        driver.get(url);
    }

    public void scrollDown(JavascriptExecutor executor) {
        executor.executeScript("window.scrollBy(0,250)");
    }

    public void scrollUp(JavascriptExecutor executor) {
        executor.executeScript("window.scroll(0, -250);");
    }

    @Override
    public void clickByClassName(WebDriver driver, String className) {
        var element = driver.findElement(By.id("APjFqb"));
        element.click();
        element.sendKeys("abcd");
    }

    @Override
    public void clickByCss(WebDriver driver, String cssSelector) {
        var element = driver.findElement(By.cssSelector(cssSelector));
        element.click();
    }

    @Override
    public void clickByXPath(WebDriver driver, String xPath) {
        var isSuccess = false;
        do {
            try {
                var element = driver.findElement(By.xpath(xPath));
                element.click();

                isSuccess = true;
            } catch (Exception ex) {
                log.error("clickByXPath failed", ex);
            }
        } while (!isSuccess);
    }

    @Override
    public void clickByXPath(WebDriver driver, String xPath, Duration duration) {
        log.info("Start clickByXPath with xPath={} in {}", xPath, duration);
        var wait = new WebDriverWait(driver, duration);
        var element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("(/html/div)[last()]")));
        log.info("Found element by xPath={}", xPath);
        // Should wait to keep ads to be shown
        ThreadHelper.waitInMs(500);
        element.click();
        log.info("End clickByXPath with xPath={}", xPath);
    }

    @Override
    public void waitNumberOfWindowsToBe(WebDriver driver, Integer number, Duration duration) {
        log.info("Start waitNumberOfWindowsToBe with {} window(s) in {}", number, duration);
        var wait = new WebDriverWait(driver, duration);
        wait.until(ExpectedConditions.numberOfWindowsToBe(number));
        log.info("End waitNumberOfWindowsToBe");
    }

    @Override
    public void waitForPageReady(WebDriver driver, Duration duration) {
        log.info("Start waitForPageReady in {}", duration);
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        log.info("End waitForPageReady");
    }
}
