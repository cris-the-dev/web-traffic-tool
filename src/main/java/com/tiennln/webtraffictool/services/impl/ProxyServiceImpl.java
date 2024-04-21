package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.services.GeoNodeService;
import com.tiennln.webtraffictool.services.ProxyService;
import com.tiennln.webtraffictool.services.WwProxyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Proxy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    private WwProxyService wwProxyService;
    private GeoNodeService geoNodeService;

    @Override
    public String getProxyUrl() {
        return wwProxyService.getProxy();
    }

    @Override
    public Proxy getProxy() {
        return geoNodeService.getProxy();
    }

    @Override
    public String getProxyPort() {
        return geoNodeService.getProxyPort();
    }

    @Override
    public String getProxyHost() {
        return geoNodeService.getProxyHost();
    }

    @Override
    public String getProxyUsername() {
        return geoNodeService.getProxyUsername();
    }

    @Override
    public String getProxyPassword() {
        return geoNodeService.getProxyPassword();
    }
}
