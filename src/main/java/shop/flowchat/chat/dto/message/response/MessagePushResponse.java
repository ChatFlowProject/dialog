package shop.flowchat.chat.dto.message.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.dto.common.Sender;
import shop.flowchat.chat.dto.common.AttachmentDto;
import shop.flowchat.chat.dto.kafka.MessagePayload;
import shop.flowchat.chat.dto.member.response.MemberSimpleResponse;

public record MessagePushResponse(
        UUID chatId,
        Sender sender,
        String content,
        List<AttachmentDto> attachments,
        LocalDateTime createdAt
) {
    public static MessagePushResponse from(MessagePayload payload, MemberSimpleResponse response) {
        return new MessagePushResponse(
            payload.chatId(),
            new Sender(response.id(), response.name(), response.avatarUrl()),
            payload.content(),
            payload.attachments(),
            payload.createdAt()
        );
    }
}