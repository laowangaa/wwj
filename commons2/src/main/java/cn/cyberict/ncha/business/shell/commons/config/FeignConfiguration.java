package cn.cyberict.ncha.business.shell.commons.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfiguration {

    private int connectTimeout = 60000;

    private int readTime = 60000;

    @Bean
    public Request.Options options(){
        return new Request.Options(connectTimeout,readTime);
    }
}
