package shop.flowchat.chat.dto.message.response;

import java.time.LocalDateTime;
import java.util.List;
import shop.flowchat.chat.dto.common.AttachmentDto;
import shop.flowchat.chat.dto.common.Sender;
import shop.flowchat.chat.dto.member.response.MemberSimpleResponse;
import shop.flowchat.chat.entity.Message;

public record MessageResponse(
    Long messageId,
    Sender sender,
    String content,
    LocalDateTime createdAt,
    Boolean isUpdated,
    Boolean isDeleted,
    List<AttachmentDto> attachments
) {
    public static MessageResponse from (Message message, MemberSimpleResponse response){
        return new MessageResponse(
                message.getId(),
                new Sender(response.id(), response.name(), response.avatarUrl()),
                message.getContent(),
                message.getCreatedAt(),
                message.getIsUpdated(),
                message.getIsDeleted(),
                AttachmentDto.from(message.getAttachments())
        );
    }
}
