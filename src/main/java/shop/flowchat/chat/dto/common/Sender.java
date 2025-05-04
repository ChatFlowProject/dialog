package shop.flowchat.chat.dto.common;

import java.util.UUID;

public record Sender(
        UUID senderId,
        String username,
        String avatarUrl
) {}
