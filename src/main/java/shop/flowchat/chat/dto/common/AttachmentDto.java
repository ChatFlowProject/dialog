package shop.flowchat.chat.dto.common;

import java.util.List;
import shop.flowchat.chat.entity.Attachment;
import shop.flowchat.chat.entity.AttachmentType;

public record AttachmentDto(
        AttachmentType type,
        String url
) {
    public static AttachmentDto from(Attachment attachment) {
        return new AttachmentDto(
                attachment.getType(),
                attachment.getUrl()
        );
    }
    public static List<AttachmentDto> from(List<Attachment> attachments) {
        return attachments.stream()
                .map(AttachmentDto::from)
                .toList();
    }
}