package shop.flowchat.chat.presentation.dto.response;

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