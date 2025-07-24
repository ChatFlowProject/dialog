package shop.flowchat.chat.external.kafka.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;
import shop.flowchat.chat.infrastructure.security.StompPrincipal;

public record MessagePayload(
        UUID senderId,
        UUID chatId,
        String content,
        List<AttachmentDto> attachments,
        LocalDateTime createdAt
) {
    public static MessagePayload from(UUID chatId, MessageCreateRequest request, StompPrincipal principal, LocalDateTime createdAt) {
        return new MessagePayload(
                principal.getId(),
                chatId,
                request.content(),
                request.attachments(),
                createdAt
        );
    }
}
