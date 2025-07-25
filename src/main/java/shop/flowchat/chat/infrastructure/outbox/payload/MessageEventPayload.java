package shop.flowchat.chat.infrastructure.outbox.payload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;

public record MessageEventPayload(
        UUID senderId,
        UUID chatId,
        String content,
        List<AttachmentDto> attachments,
        List<String> memberIds,
        LocalDateTime createdAt
) {
    public static MessageEventPayload from(UUID chatId, MessageCreateRequest request, UUID senderId) {
        return new MessageEventPayload(
                senderId,
                chatId,
                request.content(),
                request.attachments(),
                request.memberIds(),
                LocalDateTime.now()
        );
    }
}
