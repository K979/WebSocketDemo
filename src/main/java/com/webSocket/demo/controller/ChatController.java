package com.webSocket.demo.controller;

import com.webSocket.demo.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 */
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 接收客户端传递消息
     * @param chatMessage
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage){
        this.simpMessagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor simpMessageHeaderAccessor){
        simpMessageHeaderAccessor.setHeader("userName", chatMessage.getSender());
        return chatMessage;
    }

    /**
     * SendTo注解定义了消息的目的地。结合例子解释就是接收/app/chat.addUser发来的value，然后将value转发到/topic/getResponse客户端。
     * /topic/getResponse是客户端发起连接后，订阅服务端消息时指定的一个地址，用于接收服务端的返回
     * 与
     * this.simpMessagingTemplate.convertAndSend("/topic/getResponse", chatMessage); 用法相同
     */

}
