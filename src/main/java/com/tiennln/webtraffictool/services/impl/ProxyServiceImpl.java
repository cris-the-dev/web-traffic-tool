package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.clients.WwProxyClient;
import com.tiennln.webtraffictool.services.ProxyService;
import com.tiennln.webtraffictool.services.WwProxyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    private WwProxyService wwProxyService;

    @Override
    public String getProxy() {
        var proxy = wwProxyService.getProxy();
        return proxy;
    }
}
