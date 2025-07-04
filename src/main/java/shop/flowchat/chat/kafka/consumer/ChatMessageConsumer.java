package shop.flowchat.chat.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.dto.kafka.MessagePayload;
import shop.flowchat.chat.dto.message.response.MessagePushResponse;
import shop.flowchat.chat.entity.Attachment;
import shop.flowchat.chat.entity.Message;
import shop.flowchat.chat.entity.Chat;
import shop.flowchat.chat.repository.AttachmentRepository;
import shop.flowchat.chat.repository.MessageRepository;
import shop.flowchat.chat.repository.ChatRepository;
import jakarta.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {
    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final AttachmentRepository attachmentRepository;

    @KafkaListener(topics = "chat-message", groupId = "chat-group")
    public void consume(MessagePayload payload) {
        log.info("Kafka 메시지 수신 : {}", payload);

        Chat chat = chatRepository.findById(payload.chatId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        Message message = Message.create(payload, chat);
        messageRepository.save(message);

        if (payload.attachments() != null && !payload.attachments().isEmpty()) {
            java.util.List<Attachment> attachments = payload.attachments().stream()
                    .map(attachmentDto -> Attachment.create(message, attachmentDto))
                    .toList();
            attachmentRepository.saveAll(attachments);
        }

        MessagePushResponse response = MessagePushResponse.from(payload, message.getId());
        try {
            template.convertAndSend("/sub/message/" + payload.chatId(), response);
        } catch (Exception e) {
            log.warn("WebSocket 전송 실패: {}", e.getMessage());
        }
    }
}
