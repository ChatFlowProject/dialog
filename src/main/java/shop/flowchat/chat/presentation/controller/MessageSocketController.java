package shop.flowchat.chat.presentation.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.command.service.MessageCommandService;
import shop.flowchat.chat.infrastructure.kafka.dto.MessagePayload;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;
import shop.flowchat.chat.presentation.dto.request.MessageDeleteRequest;
import shop.flowchat.chat.presentation.dto.request.MessageUpdateRequest;
import shop.flowchat.chat.presentation.dto.response.MessageDeleteResponse;
import shop.flowchat.chat.presentation.dto.response.MessageUpdateResponse;
import shop.flowchat.chat.infrastructure.kafka.producer.ChatMessageProducer;
import shop.flowchat.chat.infrastructure.security.StompPrincipal;

@RestController
@RequiredArgsConstructor
public class MessageSocketController {
    private final ChatMessageProducer chatMessageProducer;
    private final MessageCommandService messageCommandService;
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

        messageCommandService.deleteMessage(request.messageId(), principal.getId());

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

        messageCommandService.updateMessage(request.messageId(), principal.getId(), request.newContent());

        template.convertAndSend("/sub/message/" + chatId,
                new MessageUpdateResponse(request.messageId(), request.newContent(), true));
    }
}
