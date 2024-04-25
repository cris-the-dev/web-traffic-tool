package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.configurations.AppConfig;
import com.tiennln.webtraffictool.services.GeoNodeService;
import com.tiennln.webtraffictool.services.ProxyService;
import com.tiennln.webtraffictool.services.WwProxyService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Proxy;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    public static ConcurrentHashMap<String, Boolean> PORT_MAP;

    private WwProxyService wwProxyService;
    private GeoNodeService geoNodeService;
    private AppConfig appConfig;

    @PostConstruct
    private void init() {
        PORT_MAP = new ConcurrentHashMap<>();
        PORT_MAP.put("9000", true);
        PORT_MAP.put("9001", true);
        PORT_MAP.put("9002", true);
        PORT_MAP.put("9003", true);
        PORT_MAP.put("9004", true);
    }

    @Override
    public Proxy getProxy() {
        if (appConfig.getProxyType().equalsIgnoreCase("premium")) {
            return geoNodeService.getProxy();
        }

        String availablePort = null;

        for (String key : PORT_MAP.keySet()) {
            if (PORT_MAP.get(key)) {
                availablePort = key;
                PORT_MAP.put(key, false);
                break;
            }
        }

        return availablePort == null
                ? null
                : geoNodeService.getProxy(availablePort);
    }

    @Override
    public String getProxyPort(Proxy proxy) {
        return proxy.getSslProxy().split(":")[1];
    }

    @Override
    public String getProxyHost(Proxy proxy) {
        return proxy.getSslProxy().split(":")[0];
    }

    @Override
    public String getProxyUsername() {
        return geoNodeService.getProxyUsername();
    }

    @Override
    public String getProxyPassword() {
        return geoNodeService.getProxyPassword();
    }

    @Override
    public void releasePort(String port) {
        PORT_MAP.put(port, true);
    }
}
