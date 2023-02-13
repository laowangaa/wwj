package cn.cyberict.ncha.business.shell.commons.config;

import feign.Logger;
import feign.Request;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
public class FeignCroFileConfiguration {

    private int connectTimeout = 60000;

    private int readTime = 60000;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    
    @Bean
    public Logger.Level Logger() {
        log.info("FeignCroFileConfiguration配置完成");
        return Logger.Level.FULL;
    }

    
    @Bean
    public Request.Options options(){
        return new Request.Options(connectTimeout,readTime);
    }

    
    @Bean
    public SpringFormEncoder feignFormEncoder(){
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}
