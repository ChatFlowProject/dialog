package shop.flowchat.chat.infrastructure.outbox.event.message;

import shop.flowchat.chat.external.kafka.dto.MessagePayload;
import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;

public class MessageCreateEvent extends OutboxEvent {

    public MessageCreateEvent(String aggregateId, MessagePayload payload) {
        super("chat", aggregateId, "messageCreate", payload, "");
    }
}
