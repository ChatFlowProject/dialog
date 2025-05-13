package shop.flowchat.chat.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.kafka.MessagePayload;
import shop.flowchat.chat.dto.message.request.MessageCreateRequest;
import shop.flowchat.chat.kafka.producer.ChatMessageProducer;

@Tag(name = "메시지 API")
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final ChatMessageProducer chatMessageProducer;

    @MessageMapping("/message/{chatId}")
    public void sendMessage(@DestinationVariable UUID chatId,
                            MessageCreateRequest request,
                            SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null || sessionAttributes.get("memberId") == null) {
            System.out.println("memberId 가 없습니다.");
            return;
        }

        UUID senderId = UUID.fromString(sessionAttributes.get("memberId").toString());
        String token = sessionAttributes.get("token").toString();
        System.out.println(token);
        MessagePayload payload = new MessagePayload(
                chatId,
                senderId,
                request.message(),
                request.attachments(),
                LocalDateTime.now(),
                false,
                token
        );
        chatMessageProducer.sendMessage(payload);
    }
}
