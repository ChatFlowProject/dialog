package shop.flowchat.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.flowchat.chat.client.MemberClient;
import shop.flowchat.chat.dto.chat.response.DmListResponse;
import shop.flowchat.chat.dto.member.MemberSimpleResponse;
import shop.flowchat.chat.entity.Chat;
import shop.flowchat.chat.entity.ChatType;
import shop.flowchat.chat.repository.ChatRepository;

import java.util.List;
import java.util.UUID;
import shop.flowchat.chat.util.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberClient memberClient;

    public List<DmListResponse> getChatsByType(String token) {
        UUID memberId = jwtTokenProvider.getMemberIdFromToken(token.replace("Bearer ", ""));
        List<Chat> chats = chatRepository.findByTypeAndMembersMemberId(ChatType.DM, memberId);

        return chats.stream()
                .map(chat -> {
                    List<DmListResponse.MemberInfo> members = chat.getMembers().stream()
                            .map(m -> {
                                MemberSimpleResponse member = memberClient.getMemberInfo(token, m.getMemberId()).data();
                                return new DmListResponse.MemberInfo(member.id(), member.name(), member.avatarUrl());
                            }).toList();
                    return new DmListResponse(chat.getId(), members);
                })
                .toList();
    }
}
