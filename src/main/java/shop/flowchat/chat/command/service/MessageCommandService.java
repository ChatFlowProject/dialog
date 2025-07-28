package shop.flowchat.chat.command.service;

import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.flowchat.chat.common.exception.custom.EntityNotFoundException;
import shop.flowchat.chat.domain.chat.Chat;
import shop.flowchat.chat.domain.message.Attachment;
import shop.flowchat.chat.domain.message.Message;
import shop.flowchat.chat.domain.member.MemberReadModel;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageEventPayload;
import shop.flowchat.chat.infrastructure.outbox.event.message.MentionCreateEvent;
import shop.flowchat.chat.infrastructure.outbox.event.message.MessageCreateEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MentionEventPayload;
import shop.flowchat.chat.infrastructure.repository.AttachmentRepository;
import shop.flowchat.chat.infrastructure.repository.ChatRepository;
import shop.flowchat.chat.infrastructure.repository.MemberReadModelRepository;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;
import shop.flowchat.chat.infrastructure.security.StompPrincipal;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;
import shop.flowchat.chat.presentation.dto.response.MessagePushResponse;

@Service
@RequiredArgsConstructor
public class MessageCommandService {
    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final AttachmentRepository attachmentRepository;
    private final MemberReadModelRepository memberRepository;

    @Transactional
    public void sendMessage(UUID chatId, MessageCreateRequest request, StompPrincipal principal) {
        LocalDateTime now = LocalDateTime.now();

        eventPublisher.publishEvent(new MessageCreateEvent(chatId.toString(), MessageEventPayload.from(chatId, request, principal.getId())));
    }

    @Transactional
    public void createMessage(MessageEventPayload payload) {
        Chat chat = chatRepository.findById(payload.chatId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        Message message = Message.create(payload, chat);
        messageRepository.save(message);

        if (payload.attachments() != null && !payload.attachments().isEmpty()) {
            List<Attachment> attachments = payload.attachments().stream()
                    .map(attachmentDto -> Attachment.create(message, attachmentDto))
                    .toList();
            attachmentRepository.saveAll(attachments);
        }

        if (payload.memberIds() != null && !payload.memberIds().isEmpty()) {
            eventPublisher.publishEvent(new MentionCreateEvent(payload.chatId().toString(), MentionEventPayload.from(message, payload)));
        }

        MemberReadModel sender = memberRepository.findById(payload.senderId())
                .orElseThrow(() -> new EntityNotFoundException("보낸 사람 정보를 찾을 수 없습니다."));

        MessagePushResponse response = MessagePushResponse.from(payload, message.getId(), sender);
        template.convertAndSend("/sub/message/" + payload.chatId(), response);
    }

    @Transactional
    public void deleteMessage(Long messageId, UUID memberId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        if (!message.getMemberId().equals(memberId)) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        message.setIsDeleted(true);
        messageRepository.save(message);
    }

    @Transactional
    public void updateMessage(Long messageId, UUID memberId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        if (!message.getMemberId().equals(memberId)) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        message.updateContent(newContent);
        messageRepository.save(message);
    }
}