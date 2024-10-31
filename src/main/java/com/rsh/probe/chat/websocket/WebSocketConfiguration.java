package com.rsh.probe.chat.websocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfiguration.class);

    @Autowired
    WebSocketHandler chatWebSocketHandler;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
 		registry
            .addHandler(chatWebSocketHandler, "/chat")
            //.setAllowedOrigins("http://localhost:8080")
            .addInterceptors(new HttpSessionHandshakeInterceptor());
 	}
    @Bean
    public static WebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler();
    }
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024);   // TODO make thiis configurable via applivcation.properties, this size has to be passed over to the client
        container.setMaxBinaryMessageBufferSize(1024);
        return container;
    }
}
