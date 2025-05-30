package shop.flowchat.chat.dto.message.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.dto.common.AttachmentDto;
import shop.flowchat.chat.entity.Message;

public record MessageCacheResponse(
        Long id,
        UUID memberId,
        String content,
        LocalDateTime createdAt,
        Boolean isUpdated,
        Boolean isDeleted,
        List<AttachmentDto> attachments
) {
    public static MessageCacheResponse from(Message message) {
        return new MessageCacheResponse(
                message.getId(),
                message.getMemberId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getIsUpdated(),
                message.getIsDeleted(),
                AttachmentDto.from(message.getAttachments())
        );
    }
}