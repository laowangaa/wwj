package cn.cyberict.websocket;

import cn.cyberict.websocket.controller.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class WebsocketApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(WebsocketApplication.class, args);
        WebSocketServer.setApplicationContext(configurableApplicationContext);
    }

}
