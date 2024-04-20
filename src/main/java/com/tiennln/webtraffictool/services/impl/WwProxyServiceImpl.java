package com.tiennln.webtraffictool.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiennln.webtraffictool.clients.WwProxyClient;
import com.tiennln.webtraffictool.helpers.ThreadHelper;
import com.tiennln.webtraffictool.models.WwProxyModel;
import com.tiennln.webtraffictool.services.WwProxyService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WwProxyServiceImpl implements WwProxyService {

    private WwProxyClient wwProxyClient;

    private static final List<String> tooManyRequestErrorCodes = List.of("0", "1");

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
            } catch (FeignException ex) {
                log.error("Caught FeignException when WwProxyService.getProxy", ex);
                if (ex instanceof FeignException.BadRequest badRequestEx) {
                    processBadRequestException(badRequestEx);
                }
            } catch (Exception ex) {
                log.error("Error when WwProxyService.getProxy", ex);
            }
        } while (!isSuccess);

        return proxy;
    }

    private void processBadRequestException(FeignException.BadRequest badRequestEx) {
        try {
            var response = new ObjectMapper().readValue(badRequestEx.contentUTF8(), WwProxyModel.class);
            if (tooManyRequestErrorCodes.contains(response.getErrorCode())) {
                log.info("Found too many request, wait for 60s");
                ThreadHelper.waitInMs(60L * 1000);
            }
        } catch (JsonProcessingException ex) {
            log.error("Error when WwProxyService.processBadRequestException", ex);
        }
    }
}
