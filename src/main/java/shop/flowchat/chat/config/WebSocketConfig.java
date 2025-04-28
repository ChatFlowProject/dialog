package shop.flowchat.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Todo: 프론트에서 SockJS 쓰는지 물어보기
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // Todo: 구독 경로 (노션에 없어서 합의 필요)
        registry.setApplicationDestinationPrefixes("/pub"); // Todo: 클라이언트 메시지 경로 (이하 동문)
    }
}
