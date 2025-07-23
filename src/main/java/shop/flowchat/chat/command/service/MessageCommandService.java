package shop.flowchat.chat.command.service;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.flowchat.chat.domain.Message;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageCommandService {
    private final MessageRepository messageRepository;

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