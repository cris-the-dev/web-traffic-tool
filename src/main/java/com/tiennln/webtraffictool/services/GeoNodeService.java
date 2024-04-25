package com.tiennln.webtraffictool.services;

import org.openqa.selenium.Proxy;

public interface GeoNodeService {
    Proxy getProxy();
    Proxy getProxy(String port);
    String getProxyUsername();
    String getProxyPassword();
}
