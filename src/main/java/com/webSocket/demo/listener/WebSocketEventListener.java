package com.webSocket.demo.listener;

import com.webSocket.demo.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * WebSocketEventListener 监听器监听连接断开事件
 */
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 新用户加入事件
     * @param sessionConnectedEvent
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent sessionConnectedEvent){
        logger.info("Received a new web socket connection");
    }

    /**
     * 用户离开事件
     * @param sessionDisconnectEvent
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent){
        logger.info("Received a web socket disconnect");
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        String username = (String) stompHeaderAccessor.getSessionAttributes().get("userName");
        if(!StringUtils.isEmpty(username)){
            logger.info("User Disconnect:" + username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setContent("User Disconnect:" + username);
            this.simpMessagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    /**
     * EventListener 注解表示注册一个事件监听器，该注解有比较重要的两个参数 class和condition ,
     * class指触发该事件监听器的事件类(事件类需要继承ApplicationEvent类),
     * condition指该事件触发时执行的方法
     * 以上参数可简写为以上 handleWebSocketConnectListener 方法形式,该方法的参数为 class,方法体为 condition
     *
     */

}
