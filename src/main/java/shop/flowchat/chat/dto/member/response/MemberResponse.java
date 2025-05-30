package shop.flowchat.chat.dto.member.response;

import java.util.List;
import java.util.UUID;

public record MemberResponse(
        UUID requester,
        List<MemberSimpleResponse> memberList
) {}