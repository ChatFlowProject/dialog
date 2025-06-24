package shop.flowchat.chat.dto.common;

import java.util.UUID;

public record Sender(
        UUID memberId,
        String name,
        String avatarUrl
) {}
