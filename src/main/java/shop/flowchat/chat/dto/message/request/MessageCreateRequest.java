package shop.flowchat.chat.dto.message.request;

import java.util.List;
import shop.flowchat.chat.dto.common.Attachment;

public record MessageCreateRequest(
        String message,
        List<Attachment> attachments
) {
}