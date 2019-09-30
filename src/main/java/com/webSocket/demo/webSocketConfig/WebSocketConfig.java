package com.webSocket.demo.webSocketConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;

@Configuration
/**
 * EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，
 * 这时候控制器（controller）
 * 开始支持@MessageMapping,就像是使用@requestMapping一样。
 */
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    ServerEndpointExporter

    /**
     * 添加一个服务端点，来接收客户端的连接。
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册socket节点 添加了一个/socket端点，客户端就可以通过这个端点来进行连接。
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端给服务端发送的前缀信息,当使用该前缀不会进入MessageMapping注解的方法(MessageMapping注解会接受以app前缀的信息),多个用逗号隔开
        registry.enableSimpleBroker("/topic");
        //定义一对一推送的时候前缀
        registry.setUserDestinationPrefix("/user");
        //服务端接收地址的前缀
        registry.setApplicationDestinationPrefixes("/app");
        /**
         *
         * registerStompEndpoints(StompEndpointRegistry registry)
         * 这个方法的作用是添加一个服务端点，来接收客户端的连接。
         *
         * registry.addEndpoint("/ws")表示添加了一个/socket端点，客户端就可以通过这个端点来进行连接。
         *
         * withSockJS()的作用是开启SockJS支持，
         *
         * configureMessageBroker(MessageBrokerRegistry registry)
         * 这个方法的作用是定义消息代理，通俗一点讲就是设置消息连接请求的各种规范信息。
         *
         * registry.enableSimpleBroker("/topic")表示客户端订阅地址的前缀信息，
         * 也就是客户端接收服务端消息的地址的前缀信息
         *
         * registry.setApplicationDestinationPrefixes("/app")指服务端接收地址的前缀，
         * 意思就是说客户端给服务端发消息的地址的前缀
         *
         */
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return true;
    }

}
