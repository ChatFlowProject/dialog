package shop.flowchat.chat.infrastructure.outbox.payload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.domain.message.Message;

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
        MentionType type,
        List<UUID> memberIds
) {
    public static MentionEventPayload from(Message message, MessageEventPayload payload) {
        MentionType mentionType;
        if (payload.memberIds() != null && payload.memberIds().size() == 1 && "everyone".equalsIgnoreCase(payload.memberIds().get(0))) {
            mentionType = MentionType.EVERYONE;
        } else if (payload.memberIds() != null && !payload.memberIds().isEmpty()) {
            mentionType = MentionType.INDIVIDUAL;
        } else {
            mentionType = null;
        }

        return new MentionEventPayload(
                message.getMemberId(),
                payload.chatId(),
                message.getId(),
                message.getContent(),
                message.getCreatedAt(),
                LocalDateTime.now(),
                message.getIsUpdated(),
                message.getIsDeleted(),
                AttachmentDto.from(message.getAttachments()),
                mentionType,
                mentionType == MentionType.INDIVIDUAL
                        ? payload.memberIds().stream().map(UUID::fromString).toList()
                        : null
        );
    }
}
