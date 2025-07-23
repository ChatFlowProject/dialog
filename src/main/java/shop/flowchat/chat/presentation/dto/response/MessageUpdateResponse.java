package shop.flowchat.chat.presentation.dto.response;

public record MessageUpdateResponse(
        Long messageId,
        String newContent,
        boolean isUpdated
) {}
