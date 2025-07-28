package shop.flowchat.chat.common.dto.value;

import java.util.UUID;
import shop.flowchat.chat.domain.member.MemberReadModel;

public record Sender(
        UUID memberId,
        String name,
        String avatarUrl
) {
    public static Sender from(MemberReadModel member) {
        return new Sender(
            member.getId(),
            member.getNickname(),
            member.getAvatarUrl()
        );
    }
}
