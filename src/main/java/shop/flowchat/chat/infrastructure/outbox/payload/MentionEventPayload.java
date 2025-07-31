package shop.flowchat.chat.infrastructure.outbox.payload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.domain.message.Message;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;

public record MentionEventPayload(
        UUID memberId,
        UUID chatId,
        Long messageId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isUpdated,
        Boolean isDeleted,
        List<AttachmentDto> attachments,
        List<String> memberIds
) {
    public static MentionEventPayload from(Message message, List<String> memberIds) {
        return new MentionEventPayload(
                message.getMemberId(),
                message.getChat().getId(),
                message.getId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getIsUpdated(),
                message.getIsDeleted(),
                AttachmentDto.from(message.getAttachments()),
                memberIds
        );
    }
}
