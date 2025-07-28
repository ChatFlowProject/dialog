package shop.flowchat.chat.external.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.flowchat.chat.domain.member.MemberReadModelState;

public record MemberEventPayload(
        UUID id,
        String nickname,
        String name,
        String avatarUrl,
        MemberReadModelState state,
        LocalDateTime createdAt,
        LocalDateTime timestamp
) {
}