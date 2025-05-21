package shop.flowchat.chat.dto.message.response;

import java.util.UUID;

public record MessageDeleteResponse(
        Long messageId,
        boolean isDeleted
) {
    public static MessageDeleteResponse from(Long messageId) {
        return new MessageDeleteResponse(
                messageId,
                true
        );
    }
}