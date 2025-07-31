package shop.flowchat.chat.infrastructure.outbox.payload;


import java.util.UUID;

public record MessageDeletePayload(
        UUID chatId,
        Long messageId,
        Boolean isDeleted
) {
    public static MessageDeletePayload from(UUID chatId, Long messageId) {
        return new MessageDeletePayload(
                chatId,
                messageId,
                true
        );
    }
}