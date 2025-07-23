package shop.flowchat.chat.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.infrastructure.client.MemberClient;
import shop.flowchat.chat.infrastructure.client.dto.response.MemberSimpleResponse;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberClient memberClient;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        System.out.println("STOMP native headers: " + accessor.toNativeHeaderMap());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println("CONNECT 명령 감지됨");

            List<String> authHeaders = accessor.getNativeHeader("Authorization");
            if (authHeaders == null || authHeaders.isEmpty()) {
                System.out.println("Authorization 헤더 없음");
                return message;
            }

            String authorizationHeader = authHeaders.get(0);
            if (!authorizationHeader.startsWith("Bearer ")) {
                System.out.println("Authorization 헤더 형식 오류 : " + authorizationHeader);
                return message;
            }

            String token = authorizationHeader.substring(7);
            System.out.println("JWT 토큰 : " + token);

            try {
                if (jwtTokenProvider.validateToken(token)) {
                    UUID memberId = jwtTokenProvider.getMemberIdFromToken(token);
                    MemberSimpleResponse member = memberClient.getMemberInfo("Bearer " + token, memberId).data();

                    System.out.println("memberId : " + member.id());
                    System.out.println("name : " + member.nickname());
                    System.out.println("avatarUrl : " + member.avatarUrl());

                    accessor.setUser(new StompPrincipal(
                        memberId,
                        member.nickname(),
                        member.avatarUrl()
                    ));

                    System.out.println("Principal 설정 완료");
                } else {
                    System.out.println("JWT 유효성 검사 실패");
                    throw new IllegalArgumentException("유효하지 않은 JWT 토큰");
                }
            } catch (Exception e) {
                System.out.println("토큰 처리 중 예외 발생 : " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }

        return message;
    }
}