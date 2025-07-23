package shop.flowchat.chat.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.Sender;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.infrastructure.kafka.dto.MessagePayload;

public record MessagePushResponse(
        Long messageId,
        UUID chatId,
        Sender sender,
        String content,
        List<AttachmentDto> attachments,
        LocalDateTime createdAt
) {
    public static MessagePushResponse from(MessagePayload payload, Long messageId) {
        return new MessagePushResponse(
            messageId,
            payload.chatId(),
            payload.sender(),
            payload.content(),
            payload.attachments(),
            payload.createdAt()
        );
    }
}