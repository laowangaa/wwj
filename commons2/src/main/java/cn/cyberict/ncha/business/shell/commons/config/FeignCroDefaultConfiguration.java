package cn.cyberict.ncha.business.shell.commons.config;

import feign.Logger;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
public class FeignCroDefaultConfiguration {

    private int connectTimeout = 60000;

    private int readTime = 60000;

    
    @Bean
    public Logger.Level Logger() {
        log.info("FeignCroDefaultConfiguration配置完成");
        return Logger.Level.FULL;
    }

    
    @Bean
    public Request.Options options(){
        return new Request.Options(connectTimeout,readTime);
    }
}
