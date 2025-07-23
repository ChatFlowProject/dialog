package shop.flowchat.chat.common.dto.value;

import java.util.UUID;

public record Sender(
        UUID memberId,
        String name,
        String avatarUrl
) {}
