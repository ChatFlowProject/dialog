package shop.flowchat.chat.dto.message.request;

import java.util.List;
import shop.flowchat.chat.dto.common.AttachmentDto;

public record MessageCreateRequest(
        String message,
        List<AttachmentDto> attachments
) {
}