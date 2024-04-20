package com.tiennln.webtraffictool.services.impl;

import com.tiennln.webtraffictool.clients.WwProxyClient;
import com.tiennln.webtraffictool.services.WwProxyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WwProxyServiceImpl implements WwProxyService {

    private WwProxyClient wwProxyClient;

    @Override
    public String getProxy() {
        var isSuccess = false;
        var proxy = "";
        do {
            try {
                var proxyData = wwProxyClient.getIp("UK-2fc331db-1721-48d1-a412-2fded7dba811", "-1");

                if (proxyData.getStatus().equalsIgnoreCase("ok")) {
                    proxy = proxyData.getData().getProxy().trim();
                    isSuccess = true;
                }
            } catch (Exception ex) {
                log.error("Error when WwProxyService.getProxy", ex);
            }
        } while (!isSuccess);

        return proxy;
    }
}
