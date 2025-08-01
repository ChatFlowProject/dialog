package shop.flowchat.chat.common.dto.value;

import java.util.List;
import shop.flowchat.chat.domain.message.Attachment;
import shop.flowchat.chat.domain.message.AttachmentType;

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
        if (attachments == null) {
            return null;
        }
        return attachments.stream()
                .map(AttachmentDto::from)
                .toList();
    }
}