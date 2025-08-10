package shop.flowchat.chat.infrastructure.outbox.payload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.common.dto.value.Sender;
import shop.flowchat.chat.domain.member.MemberReadModel;
import shop.flowchat.chat.domain.message.Message;

public record MessageCreatePayload(
        Long messageId,
        UUID chatId,
        Sender sender,
        String content,
        List<AttachmentDto> attachments,
        UUID invitedTeamId,
        LocalDateTime createdAt
) {
    public static MessageCreatePayload from(Message message, MemberReadModel sender, UUID invitedTeamId) {
        return new MessageCreatePayload(
                message.getId(),
                message.getChat().getId(),
                Sender.from(sender),
                message.getContent(),
                AttachmentDto.from(message.getAttachments()),
                invitedTeamId,
                message.getCreatedAt()
        );
    }
}
