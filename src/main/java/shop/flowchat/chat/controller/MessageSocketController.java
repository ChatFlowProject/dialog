package shop.flowchat.chat.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
import shop.flowchat.chat.security.StompPrincipal;

@RestController
@RequiredArgsConstructor
public class MessageSocketController {
    private final ChatMessageProducer chatMessageProducer;
    private final MessageService messageService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/message/{chatId}")
    public void sendMessage(@DestinationVariable UUID chatId,
                            MessageCreateRequest request,
                            StompPrincipal principal) {
        if (principal == null || principal.getName() == null) {
            System.out.println("인증되지 않은 사용자입니다.");
            return;
        }

        MessagePayload payload = MessagePayload.from(chatId, request, principal);

        chatMessageProducer.sendMessage(payload);
    }

    @MessageMapping("/message/delete/{chatId}")
    public void deleteMessage(@DestinationVariable UUID chatId,
                              MessageDeleteRequest request,
                              StompPrincipal principal) {
        if (principal == null || principal.getName() == null) {
            System.out.println("인증되지 않은 사용자입니다.");
            return;
        }

        messageService.deleteMessage(request.messageId(), principal.getId());

        MessageDeleteResponse response = MessageDeleteResponse.from(request.messageId());
        template.convertAndSend("/sub/message/" + chatId, response);
    }

    @MessageMapping("/message/update/{chatId}")
    public void updateMessage(@DestinationVariable UUID chatId,
                              MessageUpdateRequest request,
                              StompPrincipal principal) {
        if (principal == null || principal.getName() == null) {
            System.out.println("인증되지 않은 사용자입니다.");
            return;
        }

        messageService.updateMessage(request.messageId(), principal.getId(), request.newContent());

        template.convertAndSend("/sub/message/" + chatId,
                new MessageUpdateResponse(request.messageId(), request.newContent(), true));
    }
}
