package com.tiennln.webtraffictool.clients;

import com.tiennln.webtraffictool.models.WwProxyModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ww-proxy", url = "https://wwproxy.com/api/client/proxy/available")
public interface WwProxyClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    WwProxyModel getIp(@RequestParam String key, @RequestParam String provinceId);
}
