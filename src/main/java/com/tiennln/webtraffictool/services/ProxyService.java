package com.tiennln.webtraffictool.services;

import org.openqa.selenium.Proxy;

public interface ProxyService {
    String getProxyUrl();
    Proxy getProxy();
    String getProxyPort();
    String getProxyHost();
    String getProxyUsername();
    String getProxyPassword();
}
