package shop.flowchat.chat.external.client.dto.response;

import java.util.List;
import java.util.UUID;

public record MemberResponse(
        UUID requester,
        List<MemberSimpleResponse> memberList
) {}