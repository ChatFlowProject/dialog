package shop.flowchat.chat.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.kafka.MessagePayload;
import shop.flowchat.chat.dto.message.request.MessageCreateRequest;
import shop.flowchat.chat.dto.message.request.MessageDeleteRequest;
import shop.flowchat.chat.dto.message.request.MessageUpdateRequest;
import shop.flowchat.chat.dto.message.response.MessageDeleteResponse;
import shop.flowchat.chat.dto.message.response.MessageUpdateResponse;
import shop.flowchat.chat.kafka.producer.ChatMessageProducer;
import shop.flowchat.chat.service.MessageService;

@RestController
@RequiredArgsConstructor
public class MessageSocketController {
    private final ChatMessageProducer chatMessageProducer;
    private final MessageService messageService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/message/{chatId}")
    public void sendMessage(@DestinationVariable UUID chatId,
                            MessageCreateRequest request,
                            SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null || sessionAttributes.get("memberId") == null) {
            System.out.println("memberId 가 없습니다.");
            return;
        }

        UUID memberId = UUID.fromString(sessionAttributes.get("memberId").toString());
        String token = sessionAttributes.get("token").toString();
        System.out.println(token);
        MessagePayload payload = new MessagePayload(
                chatId,
                memberId,
                request.content(),
                request.attachments(),
                LocalDateTime.now(),
                token
        );
        chatMessageProducer.sendMessage(payload);
    }

    @MessageMapping("/message/delete/{chatId}")
    public void deleteMessage(@DestinationVariable UUID chatId,
                              MessageDeleteRequest request,
                              SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null || sessionAttributes.get("memberId") == null) {
            System.out.println("memberId 가 없습니다.");
            return;
        }

        UUID memberId = UUID.fromString(sessionAttributes.get("memberId").toString());

        messageService.deleteMessage(request.messageId(), memberId);

        MessageDeleteResponse response = MessageDeleteResponse.from(request.messageId());
        template.convertAndSend("/sub/message/" + chatId, response);
    }

    @MessageMapping("/message/update/{chatId}")
    public void updateMessage(@DestinationVariable UUID chatId,
                              MessageUpdateRequest request,
                              SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null || sessionAttributes.get("memberId") == null) {
            System.out.println("memberId 가 없습니다.");
            return;
        }

        UUID memberId = UUID.fromString(sessionAttributes.get("memberId").toString());

        messageService.updateMessage(request.messageId(), memberId, request.newContent());

        template.convertAndSend("/sub/message/" + chatId,
                new MessageUpdateResponse(request.messageId(), request.newContent(), true));
    }
}
