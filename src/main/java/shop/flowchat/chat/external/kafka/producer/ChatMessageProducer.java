package shop.flowchat.chat.external.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import shop.flowchat.chat.external.kafka.dto.MessagePayload;
import shop.flowchat.chat.external.kafka.dto.MentionCreateEvent;

@Service
@RequiredArgsConstructor
public class ChatMessageProducer {
    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;
    private final KafkaTemplate<String, MentionCreateEvent> mentionKafkaTemplate;
    private static final String TOPIC = "chat-message";
    private static final String MENTION_TOPIC = "mention";

    public void sendMessage(MessagePayload payload) {
        kafkaTemplate.send(TOPIC, payload.chatId().toString(), payload);
        if (payload.memberIds() != null && !payload.memberIds().isEmpty()) {
            MentionCreateEvent event = MentionCreateEvent.from(payload);
            mentionKafkaTemplate.send(MENTION_TOPIC, payload.chatId().toString(), event);
        }
    }
}
