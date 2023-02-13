package cn.cyberict.websocket.feign;

import cn.cyberict.ncha.business.commons.springconfig.FeignConfiguration;
import cn.cyberict.ncha.business.commons.utils.ResultUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "business-sso", configuration = FeignConfiguration.class, path = "/sso")
public interface LoginFeign {

    @RequestMapping(value = "/loginByUserId", method = RequestMethod.POST)
    ResultUtil loginByUserId(@RequestParam("userId") String userId);
}
