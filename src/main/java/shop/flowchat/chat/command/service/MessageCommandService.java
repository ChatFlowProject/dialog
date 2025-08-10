package shop.flowchat.chat.command.service;

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
import shop.flowchat.chat.infrastructure.outbox.event.mention.MentionDeleteEvent;
import shop.flowchat.chat.infrastructure.outbox.event.mention.MentionUpdateEvent;
import shop.flowchat.chat.infrastructure.outbox.event.message.MessageDeleteEvent;
import shop.flowchat.chat.infrastructure.outbox.event.message.MessageUpdateEvent;
import shop.flowchat.chat.infrastructure.outbox.event.teamInvite.TeamInviteCreateEvent;
import shop.flowchat.chat.infrastructure.outbox.event.teamInvite.TeamInviteDeleteEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageCreatePayload;
import shop.flowchat.chat.infrastructure.outbox.event.mention.MentionCreateEvent;
import shop.flowchat.chat.infrastructure.outbox.event.message.MessageCreateEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MentionEventPayload;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageUpdatePayload;
import shop.flowchat.chat.infrastructure.outbox.payload.TeamInviteEventPayload;
import shop.flowchat.chat.infrastructure.repository.AttachmentRepository;
import shop.flowchat.chat.infrastructure.repository.ChatRepository;
import shop.flowchat.chat.infrastructure.repository.MemberReadModelRepository;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;
import shop.flowchat.chat.presentation.dto.request.MessageCreateRequest;
import shop.flowchat.chat.presentation.dto.request.MessageDeleteRequest;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageDeletePayload;
import shop.flowchat.chat.presentation.dto.request.MessageUpdateRequest;

@Service
@RequiredArgsConstructor
public class MessageCommandService {
    private final SimpMessagingTemplate template;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final AttachmentRepository attachmentRepository;
    private final MemberReadModelRepository memberRepository;

    public void sendMessage(UUID chatId, Object payload) {
        template.convertAndSend("/sub/message/" + chatId, payload);
    }

    @Transactional
    public void createMessage(MessageCreateRequest request, UUID chatId, UUID memberId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        Message message = Message.create(request, chat, memberId);
        messageRepository.save(message);

        if (request.attachments() != null && !request.attachments().isEmpty()) {
            List<Attachment> attachments = request.attachments().stream()
                    .map(attachmentDto -> Attachment.create(message, attachmentDto))
                    .toList();
            attachmentRepository.saveAll(attachments);
        }

        MemberReadModel sender = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("보낸 사람 정보를 찾을 수 없습니다."));

        eventPublisher.publishEvent(new MessageCreateEvent(chatId.toString(), MessageCreatePayload.from(message, sender, request.invitedTeamId())));
        if (request.memberIds() != null && !request.memberIds().isEmpty()) {
            eventPublisher.publishEvent(new MentionCreateEvent(chatId.toString(), MentionEventPayload.from(message, request.memberIds())));
        } else if (request.invitedTeamId() != null) {
            eventPublisher.publishEvent(new TeamInviteCreateEvent(chatId.toString(), TeamInviteEventPayload.from(message, request.invitedTeamId())));
        }
    }

    @Transactional
    public void deleteMessage(MessageDeleteRequest request, UUID chatId, UUID memberId) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        if (!message.getMemberId().equals(memberId)) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        message.setIsDeleted(true);

        eventPublisher.publishEvent(new MessageDeleteEvent(chatId.toString(), MessageDeletePayload.from(chatId, request.messageId())));
        if (message.getContent() != null && message.getContent().contains("@")) {
            eventPublisher.publishEvent(new MentionDeleteEvent(chatId.toString(), MentionEventPayload.from(message, null)));
        } else if (message.getInvitedTeamId() != null) {
            eventPublisher.publishEvent(new TeamInviteDeleteEvent(chatId.toString(), TeamInviteEventPayload.from(message,null)));
        }
    }

    @Transactional
    public void updateMessage(MessageUpdateRequest request, UUID chatId, UUID memberId) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

        if (!message.getMemberId().equals(memberId)) {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }

        message.updateContent(request.newContent());

        eventPublisher.publishEvent(new MessageUpdateEvent(chatId.toString(), MessageUpdatePayload.from(message)));
        if (request.memberIds() != null && !request.memberIds().isEmpty()) {
            eventPublisher.publishEvent(new MentionUpdateEvent(chatId.toString(), MentionEventPayload.from(message, request.memberIds())));
        }
    }
}