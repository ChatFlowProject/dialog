package shop.flowchat.chat.dto.message.response;

public record MessageUpdateResponse(
        Long messageId,
        String newContent,
        boolean isUpdated
) {}
