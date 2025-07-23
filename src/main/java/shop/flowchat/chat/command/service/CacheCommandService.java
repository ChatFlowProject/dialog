package shop.flowchat.chat.command.service;

import feign.FeignException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shop.flowchat.chat.command.dto.CacheRequest;
import shop.flowchat.chat.common.dto.response.MessageResponse;
import shop.flowchat.chat.domain.Message;
import shop.flowchat.chat.external.client.MemberClient;
import shop.flowchat.chat.external.client.dto.request.MemberListRequest;
import shop.flowchat.chat.external.client.dto.response.MemberResponse;
import shop.flowchat.chat.external.client.dto.response.MemberSimpleResponse;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class CacheCommandService {
    private final MessageRepository messageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberClient memberClient;

    public void cacheMessagesByUser(CacheRequest request) {
        UUID memberId = request.memberId();

        for (CacheRequest.Channel channel : request.channels()) {
            Long channelId = channel.channelId();
            UUID chatId = channel.chatId();
            Long lastMessageId = channel.lastMessageId();

            PageRequest pageRequest = PageRequest.of(0, 30);
            List<Message> messages = messageRepository.findByChatIdAndIdLessThanOrderByIdDesc(chatId, lastMessageId, pageRequest);
            Collections.reverse(messages);

            Map<UUID, MemberSimpleResponse> memberMap = getMemberMap(null, messages);

            List<MessageResponse> messageResponses = messages.stream()
                    .map(m -> MessageResponse.from(m, memberMap.get(m.getMemberId())))
                    .toList();

            String key = memberId + ":" + channelId;
            redisTemplate.opsForList().rightPushAll(key, messageResponses);
            redisTemplate.opsForList().trim(key, -30, -1);
        }
    }

    private Map<UUID, MemberSimpleResponse> getMemberMap(String token, List<Message> messages) {
        Set<UUID> memberIds = messages.stream()
                .map(Message::getMemberId)
                .collect(Collectors.toSet());

        MemberListRequest request = new MemberListRequest(new ArrayList<>(memberIds));
        MemberResponse memberResponses;
        try {
            memberResponses = memberClient.getMemberInfoList(token, request).data();
        } catch (FeignException e) {
            throw new RuntimeException("회원 정보 조회에 실패했습니다.", e);
        }

        return memberResponses.memberList().stream()
                .collect(Collectors.toMap(MemberSimpleResponse::id, m -> m));
    }
}
