package shop.flowchat.chat.infrastructure.outbox.payload;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.flowchat.chat.domain.message.Message;

public record TeamInviteEventPayload(
        UUID memberId,
        UUID chatId,
        Long messageId,
        String content,
        UUID invitedTeamId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TeamInviteEventPayload from(Message message, UUID invitedTeamId) {
        return new TeamInviteEventPayload(
                message.getMemberId(),
                message.getChat().getId(),
                message.getId(),
                message.getContent(),
                invitedTeamId,
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
