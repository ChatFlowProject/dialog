package shop.flowchat.chat.infrastructure.outbox.event.message;

import shop.flowchat.chat.infrastructure.outbox.payload.MessageCreatePayload;
import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;

public class MessageCreateEvent extends OutboxEvent {

    public MessageCreateEvent(String aggregateId, MessageCreatePayload payload) {
        super("chat", aggregateId, "messageCreate", payload, "");
    }
}
