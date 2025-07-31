package shop.flowchat.chat.infrastructure.outbox.payload;

import java.util.UUID;
import shop.flowchat.chat.domain.message.Message;

public record MessageUpdatePayload(
        UUID chatId,
        Long messageId,
        String newContent,
        Boolean isUpdated
) {
    public static MessageUpdatePayload from(Message message) {
        return new MessageUpdatePayload(
                message.getChat().getId(),
                message.getId(),
                message.getContent(),
                message.getIsUpdated()
        );
    }
}
