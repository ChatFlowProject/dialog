package shop.flowchat.chat.dto.message.request;

public record MessageUpdateRequest(
        Long messageId,
        String newContent
) {}