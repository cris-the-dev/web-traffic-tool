package com.tiennln.webtraffictool.services;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public interface SeleniumService {

    WebDriver getDriver();

    void openBrowser(WebDriver driver, String url);

    void scrollDown(JavascriptExecutor executor);

    void scrollUp(JavascriptExecutor executor);
    void clickByClassName(WebDriver driver, String className);
    void clickByCss(WebDriver driver, String cssSelector);
    void clickByXPath(WebDriver driver, String xPath);
}
