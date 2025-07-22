package shop.flowchat.chat.dto.kafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MentionCreateEvent(
        UUID senderId,
        UUID chatId,
        Long messageId,
        String content,
        MentionType type,
        List<UUID> memberIds,
        LocalDateTime createdAt
) {
    public static MentionCreateEvent from(MessagePayload payload) {
        MentionType mentionType;
        if (payload.memberIds() != null && payload.memberIds().size() == 1 && "everyone".equalsIgnoreCase(payload.memberIds().get(0))) {
            mentionType = MentionType.EVERYONE;
        } else if (payload.memberIds() != null && !payload.memberIds().isEmpty()) {
            mentionType = MentionType.INDIVIDUAL;
        } else {
            mentionType = null;
        }

        return new MentionCreateEvent(
                payload.sender().memberId(),
                payload.chatId(),
                null,
                payload.content(),
                mentionType,
                mentionType == MentionType.INDIVIDUAL
                        ? payload.memberIds().stream().map(UUID::fromString).toList()
                        : null,
                payload.createdAt()
        );
    }
}
