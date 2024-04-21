package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.services.GeoNodeService;
import org.openqa.selenium.Proxy;
import org.springframework.stereotype.Service;

@Service
public class GeoNodeServiceImpl implements GeoNodeService {

    private static final String PROXY_HOST = "us.premium-residential.geonode.com";
    private static final String PROXY_PORT = "9000";
    private static final String PROXY_USERNAME = "geonode_W1gQvT4Z6r";
    private static final String PROXY_PWD = "973aca9e-1091-4c03-bf83-7ea591f6ec12";

    @Override
    public Proxy getProxy() {
        var proxyServer = PROXY_HOST + ":" + PROXY_PORT;
        var proxyAuth = PROXY_USERNAME + ":" + PROXY_PWD;

        var proxy = new Proxy();
        proxy.setHttpProxy(proxyServer);
        proxy.setSslProxy(proxyServer);

        return proxy;
    }
}
