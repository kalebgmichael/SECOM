package com.productcnit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");
//        // Set the application destination prefix to match the API gateway mapping
//        config.setApplicationDestinationPrefixes("/api/socket");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // Register the Stomp endpoint to match the API gateway path
//        registry.addEndpoint("/api/socket")
//                .setAllowedOrigins("*") // Specify allowed origins if needed
//                .withSockJS();
//    }
//}

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/specific");   // enabling broker @SendTo("/topic/blabla")
        config.setApplicationDestinationPrefixes("/chat-app");  // prefix for incoming messages in @MessageMapping
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("api/socket/chat-websocket")
        registry.addEndpoint("/chat-websocket")
                .setAllowedOriginPatterns("*")
//                .setHandshakeHandler(new UserHandshakeHandler())
                .withSockJS();
    }
}
