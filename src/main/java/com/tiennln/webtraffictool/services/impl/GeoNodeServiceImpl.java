package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.configurations.AppConfig;
import com.tiennln.webtraffictool.services.GeoNodeService;
import lombok.AllArgsConstructor;
import org.openqa.selenium.Proxy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeoNodeServiceImpl implements GeoNodeService {

    private static final String PREMIUM_PROXY_HOST = "us.premium-residential.geonode.com";
    private static final String DEDICATED_PROXY_HOST = "dedicate-residential.geonode.com";
    private static final String PROXY_PORT = "9000";
    private static final String PROXY_USERNAME = "geonode_W1gQvT4Z6r";
    private static final String PROXY_PWD = "973aca9e-1091-4c03-bf83-7ea591f6ec12";

    private AppConfig appConfig;

    @Override
    public Proxy getProxy() {
        var proxyServer = PREMIUM_PROXY_HOST + ":" + PROXY_PORT;

        var proxy = new Proxy();
        proxy.setHttpProxy(proxyServer);
        proxy.setSslProxy(proxyServer);

        return proxy;
    }

    @Override
    public Proxy getProxy(String port) {
        var proxyHost = "premium".equalsIgnoreCase(appConfig.getProxyType())
                ? PREMIUM_PROXY_HOST
                : DEDICATED_PROXY_HOST;

        var proxyServer = proxyHost + ":" + port;

        var proxy = new Proxy();
        proxy.setHttpProxy(proxyServer);
        proxy.setSslProxy(proxyServer);

        return proxy;
    }

    @Override
    public String getProxyUsername() {
        return PROXY_USERNAME;
    }

    @Override
    public String getProxyPassword() {
        return PROXY_PWD;
    }
}
