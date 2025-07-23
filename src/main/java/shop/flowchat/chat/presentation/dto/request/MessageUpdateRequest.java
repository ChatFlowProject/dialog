package shop.flowchat.chat.presentation.dto.request;

public record MessageUpdateRequest(
        Long messageId,
        String newContent
) {}