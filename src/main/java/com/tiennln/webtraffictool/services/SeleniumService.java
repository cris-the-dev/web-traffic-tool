package com.tiennln.webtraffictool.services;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public interface SeleniumService {

    WebDriver getDriver(Proxy proxy);

    void openBrowser(WebDriver driver, String url);

    void scrollDown(JavascriptExecutor executor);

    void scrollUp(JavascriptExecutor executor);
    void clickByClassName(WebDriver driver, String className);
    void clickByCss(WebDriver driver, String cssSelector);
    void clickByXPath(WebDriver driver, String xPath);
    boolean clickByXPath(WebDriver driver, String xPath, Duration timeout, Integer numOfRetries);
    void waitNumberOfWindowsToBe(WebDriver driver, Integer number, Duration timeout);
    boolean waitForPageReady(WebDriver driver, Duration timeout, Integer numOfRetries, Runnable onError);
    boolean switchToFrame(WebDriver driver, String xPath, Duration timeout, Integer numOfRetries);
    boolean search(WebDriver driver, String xPath, Duration timeout, String searchText);
}
