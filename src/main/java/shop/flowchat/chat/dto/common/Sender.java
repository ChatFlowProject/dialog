package shop.flowchat.chat.dto.common;

public record Sender(
        Long userId,
        String username,
        String avatarUrl
) {}
