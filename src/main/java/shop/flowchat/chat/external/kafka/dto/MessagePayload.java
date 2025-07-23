package shop.flowchat.chat.external.kafka.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.common.dto.value.Sender;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;
import shop.flowchat.chat.infrastructure.security.StompPrincipal;

public record MessagePayload(
        UUID chatId,
        Sender sender,
        String content,
        List<AttachmentDto> attachments,
        List<String> memberIds,
        LocalDateTime createdAt
) {
    public static MessagePayload from(UUID chatId, MessageCreateRequest request, StompPrincipal principal) {
        return new MessagePayload(
                chatId,
                new Sender(principal.getId(), principal.getName(), principal.getAvatarUrl()),
                request.content(),
                request.attachments(),
                request.memberIds(),
                LocalDateTime.now()
        );
    }
}
