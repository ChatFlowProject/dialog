package shop.flowchat.chat.external.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.common.exception.custom.EntityNotFoundException;
import shop.flowchat.chat.domain.readmodel.MemberReadModel;
import shop.flowchat.chat.external.kafka.dto.MessagePayload;
import shop.flowchat.chat.infrastructure.repository.MemberReadModelRepository;
import shop.flowchat.chat.presentation.dto.response.MessagePushResponse;
import shop.flowchat.chat.domain.message.Attachment;
import shop.flowchat.chat.domain.message.Message;
import shop.flowchat.chat.domain.chat.Chat;
import shop.flowchat.chat.infrastructure.repository.AttachmentRepository;
import shop.flowchat.chat.infrastructure.repository.MessageRepository;
import shop.flowchat.chat.infrastructure.repository.ChatRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final AttachmentRepository attachmentRepository;
    private final MemberReadModelRepository memberRepository;

    @KafkaListener(topics = "chat")
    public void consume(String rawMessage) {
        try {
            MessagePayload payload = objectMapper.readValue(rawMessage, MessagePayload.class);
            log.info("Kafka 메시지 수신 및 역직렬화 완료: {}", payload);

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

            MemberReadModel sender = memberRepository.findById(payload.senderId())
                    .orElseThrow(() -> new EntityNotFoundException("보낸 사람 정보를 찾을 수 없습니다."));

            MessagePushResponse response = MessagePushResponse.from(payload, message.getId(), sender);
            template.convertAndSend("/sub/message/" + payload.chatId(), response);

        } catch (Exception e) {
            log.error("❌ Kafka 메시지 처리 중 오류 발생", e);
        }
    }
}
