package shop.flowchat.chat.common.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.common.dto.value.AttachmentDto;
import shop.flowchat.chat.common.dto.value.Sender;
import shop.flowchat.chat.domain.member.MemberReadModel;
import shop.flowchat.chat.domain.message.Message;

public record MessageResponse(
    Long messageId,
    Sender sender,
    String content,
    LocalDateTime createdAt,
    Boolean isUpdated,
    Boolean isDeleted,
    List<AttachmentDto> attachments,
    UUID invitedTeamId
) {
    public static MessageResponse from (Message message, MemberReadModel member){
        return new MessageResponse(
                message.getId(),
                Sender.from(member),
                message.getContent(),
                message.getCreatedAt(),
                message.getIsUpdated(),
                message.getIsDeleted(),
                AttachmentDto.from(message.getAttachments()),
                message.getInvitedTeamId()
        );
    }
}
