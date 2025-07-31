package shop.flowchat.chat.presentation.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.command.service.MessageCommandService;
import shop.flowchat.chat.common.exception.custom.AuthorizationException;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;
import shop.flowchat.chat.presentation.dto.request.MessageDeleteRequest;
import shop.flowchat.chat.presentation.dto.request.MessageUpdateRequest;
import shop.flowchat.chat.infrastructure.security.StompPrincipal;

@RestController
@RequiredArgsConstructor
public class MessageSocketController {
    private final MessageCommandService messageCommandService;

    @MessageMapping("/message/{chatId}")
    public void sendMessage(@DestinationVariable UUID chatId,
                            MessageCreateRequest request,
                            StompPrincipal principal) {
        if (principal == null || principal.getName() == null) {
            throw new AuthorizationException("인증되지 않은 사용자입니다.");
        }
        messageCommandService.createMessage(request, chatId, principal.getId());
    }

    @MessageMapping("/message/delete/{chatId}")
    public void deleteMessage(@DestinationVariable UUID chatId,
                              MessageDeleteRequest request,
                              StompPrincipal principal) {
        if (principal == null || principal.getName() == null) {
            System.out.println("인증되지 않은 사용자입니다.");
            return;
        }
        messageCommandService.deleteMessage(request, chatId, principal.getId());
    }

    @MessageMapping("/message/update/{chatId}")
    public void updateMessage(@DestinationVariable UUID chatId,
                              MessageUpdateRequest request,
                              StompPrincipal principal) {
        if (principal == null || principal.getName() == null) {
            System.out.println("인증되지 않은 사용자입니다.");
            return;
        }
        messageCommandService.updateMessage(request, chatId, principal.getId());
    }
}
