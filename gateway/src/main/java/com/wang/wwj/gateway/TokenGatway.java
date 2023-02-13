package com.wang.wwj.gateway;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Component
@CrossOrigin
public class TokenGatway implements GlobalFilter, Ordered , ApplicationRunner {

    @Autowired
    RedisTemplate redisTemplate;
    @Value("${passuri.path}")
    private String passuriPath;

    private static List<String> passUris = new ArrayList<>();

    Logger logger = LoggerFactory.getLogger(TokenGatway.class);
    @Autowired
    private HttpClientUtil httpClientUtil;

    private final ObjectProvider<DispatcherHandler> dispatcherHandler;

    public TokenGatway(ObjectProvider<DispatcherHandler> dispatcherHandler) {
        this.dispatcherHandler = dispatcherHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestUri = request.getURI();
        HttpHeaders headers = request.getHeaders();
        List<String> authorization = headers.get("Authorization");
        
        if (validateUrlMatchInitUrl(requestUri.getPath())) {
            return chain.filter(exchange);
        }
        String token = null;
        if (null != authorization && !authorization.isEmpty()) {
            for (String tokens : authorization) {
                token = tokens;
            }
            
            if (StringUtils.isNotEmpty(token) && valiteToken(token)) {
                redisTemplate.expire(token, 8 * 60 * 60L, TimeUnit.SECONDS);
                return chain.filter(exchange);
            }
        }
        return Mono.defer(() -> {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            final ServerHttpResponse response = exchange.getResponse();
            String bytes = "{\"code\":\"401\",\"message\":\"非法访问,没有检测到token~~~~~~\",\"url\":\"" + requestUri + "\"}";
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes.getBytes());
            logger.info("TokenFilter拦截非法请求，没有检测到token............" + "url:" + requestUri);
            return response.writeWith(Flux.just(buffer));
        });
    }


    
    public boolean validateUrlMatchInitUrl(String uri) {
        String realUri = uri.split("\\?")[0];
        
        if (passUris.contains(realUri)) {
            return true;
        }
        for (String passUri : passUris) {
            if (passUri.endsWith("*")) {
                String preUri = passUri.substring(0, passUri.length() - 2);
                if (realUri.indexOf(preUri) == 0 || realUri.indexOf(preUri) == 1) {
                    return true;
                }
            }else if (passUri.startsWith("*")) {
                String preUri = passUri.substring(2, passUri.length());
                if (realUri.endsWith(preUri)) {
                    return true;
                }
            }
        }
        return false;
    }

    
    public Boolean valiteToken(String token) {
        DataType type = redisTemplate.type(token);
        Object sToken = null;
        if ("string".equals(type.code())) {
            sToken = redisTemplate.opsForValue().get(token);
        }
        if ("hash".equals(type.code())) {
            sToken = redisTemplate.opsForHash().entries(token);
        }
        if (sToken != null) {
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            SAXReader reader = new SAXReader();
            Resource resource = new ClassPathResource(passuriPath);
            Document document = reader.read(new InputStreamReader(resource.getInputStream(), "UTF-8"));
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            while (iterator.hasNext()) {
                Element uri = (Element) iterator.next();
                passUris.add(uri.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
