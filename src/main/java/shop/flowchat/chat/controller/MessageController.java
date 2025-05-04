package shop.flowchat.chat.controller;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.kafka.MessagePayload;
import shop.flowchat.chat.dto.message.request.MessageCreateRequest;
import shop.flowchat.chat.kafka.producer.ChatMessageProducer;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final ChatMessageProducer chatMessageProducer;

    @MessageMapping("/message/{chatId}")
    public void sendMessage(@DestinationVariable UUID chatId, MessageCreateRequest request, SimpMessageHeaderAccessor headerAccessor) {
        UUID senderId = UUID.fromString(headerAccessor.getSessionAttributes().get("memberId").toString());
        MessagePayload payload = new MessagePayload(
                chatId,
                senderId,
                request.message(),
                request.attachments(),
                LocalDateTime.now(),
                false
        );
        chatMessageProducer.sendMessage(payload);
    }
}
