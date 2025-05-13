package shop.flowchat.chat.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import shop.flowchat.chat.dto.kafka.MessagePayload;
import shop.flowchat.chat.dto.member.MemberSimpleResponse;
import shop.flowchat.chat.dto.message.response.MessagePushResponse;
import shop.flowchat.chat.client.MemberClient;
import shop.flowchat.chat.entity.Message;
import shop.flowchat.chat.entity.Chat;
import shop.flowchat.chat.repository.MessageRepository;
import shop.flowchat.chat.repository.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import feign.FeignException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {
    private final SimpMessagingTemplate template;
    private final MemberClient memberClient;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @KafkaListener(topics = "chat-message", groupId = "chat-group")
    public void consume(MessagePayload payload) {
        log.info("Kafka 메시지 수신 : {}", payload);

        Chat chat = chatRepository.findById(payload.chatId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

         MemberSimpleResponse member;
         try {
             String bearerToken = payload.token().startsWith("Bearer ")
                     ? payload.token()
                     : "Bearer " + payload.token();

             member = memberClient.getMemberInfo(bearerToken, payload.senderId()).data();
         } catch (FeignException e) {
             log.warn("멤버 조회 실패: {}", e.getMessage());
             return;
         }

        Message message = Message.create(payload, chat);
        messageRepository.save(message);

        MessagePushResponse response = MessagePushResponse.from(payload, member);
        try {
            template.convertAndSend("/sub/message/" + payload.chatId(), response);
        } catch (Exception e) {
            log.warn("WebSocket 전송 실패: {}", e.getMessage());
        }
    }
}
