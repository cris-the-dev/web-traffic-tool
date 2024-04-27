package com.tiennln.webtraffictool.services;

import org.openqa.selenium.Proxy;

public interface ProxyService {
    Proxy getProxy();
    Proxy getProxy(String port);
    String getProxyPort(Proxy proxy);
    String getProxyHost(Proxy proxy);
    String getProxyUsername();
    String getProxyPassword();
    void releasePort(String port);
}
