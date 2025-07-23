package shop.flowchat.chat.query;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.flowchat.chat.external.client.MemberClient;
import shop.flowchat.chat.common.dto.value.Direction;
import shop.flowchat.chat.external.client.dto.request.MemberListRequest;
import shop.flowchat.chat.external.client.dto.response.MemberResponse;
import shop.flowchat.chat.external.client.dto.response.MemberSimpleResponse;
import shop.flowchat.chat.common.dto.response.MessageResponse;
import shop.flowchat.chat.domain.Message;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageQueryService {

    private final MessageRepository messageRepository;
    private final MemberClient memberClient;

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByDirection(String token, UUID chatId, Long messageId, Direction direction, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Message> messages;

        if (direction == Direction.UP) {
            messages = messageRepository.findByChatIdAndIdLessThanOrderByIdDesc(chatId, messageId, pageRequest);
        } else if (direction == Direction.INCLUSIVE_UP) {
            messages = messageRepository.findByChatIdAndIdLessThanEqualOrderByIdDesc(chatId, messageId, pageRequest);
        } else {
            messages = messageRepository.findByChatIdAndIdGreaterThanOrderByIdAsc(chatId, messageId, pageRequest);
        }

        Map<UUID, MemberSimpleResponse> memberMap = getMemberMap(token, messages);

        return messages.stream()
                .map(message -> MessageResponse.from(message, memberMap.get(message.getMemberId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getLatestMessages(String token, UUID chatId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Message> messages = messageRepository.findByChatIdOrderByIdDesc(chatId, pageRequest);
        Collections.reverse(messages);

        Map<UUID, MemberSimpleResponse> memberMap = getMemberMap(token, messages);

        return messages.stream()
                .map(message -> MessageResponse.from(message, memberMap.get(message.getMemberId())))
                .toList();
    }

    private Map<UUID, MemberSimpleResponse> getMemberMap(String token, List<Message> messages) {
        Set<UUID> memberIds = messages.stream()
                .map(Message::getMemberId)
                .collect(Collectors.toSet());

        MemberListRequest request = new MemberListRequest(new ArrayList<>(memberIds));
        MemberResponse memberResponses = memberClient.getMemberInfoList(token, request).data();

        return memberResponses.memberList().stream()
                .collect(Collectors.toMap(MemberSimpleResponse::id, m -> m));
    }
}