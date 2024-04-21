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
        var proxy = wwProxyService.getProxy();
        return proxy;
    }

    @Override
    public Proxy getProxy() {
        return geoNodeService.getProxy();
    }
}
