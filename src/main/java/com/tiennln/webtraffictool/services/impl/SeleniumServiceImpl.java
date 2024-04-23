package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.helpers.FileHelper;
import com.tiennln.webtraffictool.helpers.ThreadHelper;
import com.tiennln.webtraffictool.services.ProxyService;
import com.tiennln.webtraffictool.services.SeleniumService;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
@Slf4j
public class SeleniumServiceImpl implements SeleniumService {

    private ProxyService proxyService;

    public WebDriver getDriver() {
        // Download driver
        var driverManager = WebDriverManager.chromedriver();
        var driverManagerCfg = driverManager.config();
        driverManagerCfg.setCachePath("./driver");
        driverManager.browserVersion("123").setup();

        // Get proxy
        var proxy = proxyService.getProxy();

        // Set options
        var options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--headless=new");
        options.setBrowserVersion(driverManagerCfg.getChromeVersion());
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.setCapability("proxy", proxy);

        // Create a map to store  preferences
        var prefs = new HashMap<String, Object>();
        // 1: allow, 2: block
        prefs.put("profile.default_content_setting_values.notifications", 1);
        options.setExperimentalOption("prefs", prefs);

        // Set bypass proxy file
        var bypassProxyFile = getBypassProxyExtensionFile(
                proxyService.getProxyHost(),
                proxyService.getProxyPort(),
                proxyService.getProxyUsername(),
                proxyService.getProxyPassword()
        );
        if (bypassProxyFile != null) {
            options.addExtensions(bypassProxyFile);
        }

        log.info("Setup with option {}", options);

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
    public boolean clickByXPath(WebDriver driver, String xPath, Duration timeout, Integer numOfRetries) {
        boolean isSuccess = false;
        do {
            numOfRetries -= 1;
            try {
                log.info("Start clickByXPath with xPath={} in {}", xPath, timeout);
                var wait = new WebDriverWait(driver, timeout);
                var element = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
                log.info("Found element by xPath={}", xPath);
                // Should wait to keep ads to be shown
                ThreadHelper.waitInMs(500);
                element.click();
                log.info("End clickByXPath with xPath={}", xPath);
                isSuccess = true;
            } catch (TimeoutException ex) {
                if (numOfRetries > 0) {
                    log.error("Caught timeout exception when clickByXPath-> retry: {}", numOfRetries, ex);
                    driver.navigate().refresh();
                    ThreadHelper.waitInMs(500);
                }
            } catch (Exception ex) {
                log.error("Caught exception when clickByXPath", ex);
            }
        } while (numOfRetries > 0 && !isSuccess);
        return isSuccess;
    }

    @Override
    public void waitNumberOfWindowsToBe(WebDriver driver, Integer number, Duration timeout) {
        log.info("Start waitNumberOfWindowsToBe with {} window(s) in {}", number, timeout);
        var wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.numberOfWindowsToBe(number));
        log.info("End waitNumberOfWindowsToBe");
    }

    @Override
    public boolean waitForPageReady(WebDriver driver, Duration timeout, Integer numOfRetries, Runnable onError) {
        boolean isSuccess = false;
        do {
            numOfRetries -= 1;
            try {
                log.info("Start waitForPageReady in {}", timeout);
                log.info("Current url {}", driver.getCurrentUrl());
                var wait = new WebDriverWait(driver, timeout);
                wait.until(
                        webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                log.info("End waitForPageReady");
                isSuccess = true;
            } catch (TimeoutException ex) {
                log.error("Caught timeout exception when waitForPageReady -> retry: {}", numOfRetries, ex);
                onError.run();
                ThreadHelper.waitInMs(500);
            } catch (Exception ex) {
                log.error("Caught exception when waitForPageReady", ex);
            }
        } while (numOfRetries > 0 && !isSuccess);
        return isSuccess;
    }

    @Override
    public boolean switchToFrame(WebDriver driver, String xPath, Duration timeout, Integer numOfRetries) {
        boolean isSuccess = false;
        do {
            numOfRetries -= 1;
            try {
                log.info("Start switchToFrame with {} in {}", xPath, timeout);
                var wait = new WebDriverWait(driver, timeout);
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(xPath)));
                log.info("End switchToFrame");
                isSuccess = true;
            } catch (TimeoutException ex) {
                log.error("Caught timeout exception when switchToFrame -> retry: {}", numOfRetries, ex);
                driver.navigate().refresh();
                ThreadHelper.waitInMs(500);
            } catch (Exception ex) {
                log.error("Caught exception when switchToFrame", ex);
            }
        } while (numOfRetries > 0 && !isSuccess);
        return isSuccess;
    }

    @Override
    public boolean search(WebDriver driver, String xPath, Duration timeout, String searchText) {
        var wait = new WebDriverWait(driver, timeout);
        var element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        element.sendKeys(searchText);
        ThreadHelper.waitInMs(100);
        element.sendKeys(Keys.ENTER);
        return true;
    }

    private File getBypassProxyExtensionFile(String host, String port, String username, String password) {
        if (Stream.of(host, port, username, password).anyMatch(StringUtils::isEmpty)) {
            return null;
        }
        var file = new File("proxy_auth_plugin.zip");
        if (file.exists()) {
            return file;
        }
        var manifestJson = """
                {
                  "version": "1.0.0",
                  "manifest_version": 2,
                  "name": "Chrome Proxy",
                  "permissions": [
                    "proxy",
                    "tabs",
                    "unlimitedStorage",
                    "storage",
                    "<all_urls>",
                    "webRequest",
                    "webRequestBlocking"
                  ],
                  "background": {
                    "scripts": ["background.js"]
                  },
                  "minimum_chrome_version":"22.0.0"
                }""".trim();

        var backgroundJs = String.format("""
                var config = {
                    mode: "fixed_servers",
                    rules: {
                        singleProxy: {
                            scheme: "http",
                            host: "%s",
                            port: parseInt(%s)
                        },
                        bypassList: ["localhost"]
                    }
                };
                chrome.proxy.settings.set({value: config, scope: "regular"}, function() {});
                function callbackFn(details) {
                    return {
                        authCredentials: {
                            username: "%s",
                            password: "%s"
                        }
                    };
                }
                chrome.webRequest.onAuthRequired.addListener(
                    callbackFn,
                    {urls: ["<all_urls>"]},
                    ['blocking']
                );
                """.trim(), host, port, username, password);

        try {
            var fos = new FileOutputStream("proxy_auth_plugin.zip");
            var zipOS = new ZipOutputStream(fos);

            FileHelper.createFile("manifest.json", manifestJson);
            FileHelper.createFile("background.js", backgroundJs);


            FileHelper.writeToZipFile("manifest.json", zipOS);
            FileHelper.writeToZipFile("background.js", zipOS);
            zipOS.close();
            fos.close();
            return file;
        } catch (Exception ex) {
            log.error("Caught error in getBypassProxyExtensionFile", ex);
        }
        return null;
    }
}
