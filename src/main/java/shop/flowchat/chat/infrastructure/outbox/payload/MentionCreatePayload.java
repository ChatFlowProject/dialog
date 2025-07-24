package shop.flowchat.chat.infrastructure.outbox.payload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.infrastructure.security.StompPrincipal;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;

public record MentionCreatePayload(
        UUID senderId,
        UUID chatId,
        Long messageId,
        String content,
        MentionType type,
        List<UUID> memberIds,
        LocalDateTime createdAt
) {
    public static MentionCreatePayload from(UUID chatId, MessageCreateRequest request, StompPrincipal principal, LocalDateTime createdAt) {
        MentionType mentionType;
        if (request.memberIds() != null && request.memberIds().size() == 1 && "everyone".equalsIgnoreCase(request.memberIds().get(0))) {
            mentionType = MentionType.EVERYONE;
        } else if (request.memberIds() != null && !request.memberIds().isEmpty()) {
            mentionType = MentionType.INDIVIDUAL;
        } else {
            mentionType = null;
        }

        return new MentionCreatePayload(
                principal.getId(),
                chatId,
                null,
                request.content(),
                mentionType,
                mentionType == MentionType.INDIVIDUAL
                        ? request.memberIds().stream().map(UUID::fromString).toList()
                        : null,
                createdAt
        );
    }
}
