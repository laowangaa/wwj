package cn.cyberict.websocket.controller;

import cn.cyberict.ncha.business.commons.utils.ResultUtil;
import cn.cyberict.websocket.feign.LoginFeign;
import cn.cyberict.websocket.utils.QRCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/socketServer")
@Component
public class WebSocketServer {


    private static ApplicationContext applicationContext;


    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketServer.applicationContext = applicationContext;
    }


    
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    
    
    private static ConcurrentHashMap<Session, WebSocketServer> webSocketMap = new ConcurrentHashMap();

    private static ConcurrentHashMap<String, Session> uuMap = new ConcurrentHashMap();
    


    private static Vector sessionList = new Vector();


    
    private Session session;

    private static final String  qrCodeUrlDep = "c:zswl/";
    private static final String  qrCodeUrlPro = "/zhxzgl/";

    
    @OnOpen
    public void onOpen(Session session) throws Exception {
        String uuid = UUID.randomUUID().toString();

        String imagePath = qrCodeUrlPro + LocalDate.now() + "/" + uuid + ".jpg";
        QRCodeUtil.encode(uuid, null, imagePath, true);

        InputStream in = null;
        byte[] data = null;
        
        try {
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        this.session = session;
        uuMap.put(uuid, session);
        sessionList.add(session);
        webSocketMap.put(session, this);


        sendMessage(Base64.encodeBase64String(data));

    }

    
    @OnClose
    public void onClose(Session session, @PathParam("uuid") String uuid) {
        sessionList.remove(session);




        subOnlineCount();           
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    

    @OnMessage
    public void onMessage(String message) throws IOException {

        System.out.println("来自客户端的消息:" + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        JSONObject jsonObject1 = JSON.parseObject(jsonObject.getString("message"));
        String userId = jsonObject1.getString("userId");
        String uuid = jsonObject1.getString("uuid");
        LoginFeign loginFeign = applicationContext.getBean(LoginFeign.class);
        ResultUtil resultUtil = loginFeign.loginByUserId(userId);
        Map dataMap = (Map) resultUtil.getData();
        if (sessionList != null) {
            Session session1 = uuMap.get(uuid);
            webSocketMap.get(session1).sendMessage(JSONObject.toJSONString(dataMap));
        }

    }

    
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        onlineCount.getAndDecrement();
    }
}
