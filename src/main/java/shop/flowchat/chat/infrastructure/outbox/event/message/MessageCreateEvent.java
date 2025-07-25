package shop.flowchat.chat.infrastructure.outbox.event.message;

import shop.flowchat.chat.infrastructure.outbox.payload.MessageEventPayload;
import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;

public class MessageCreateEvent extends OutboxEvent {

    public MessageCreateEvent(String aggregateId, MessageEventPayload payload) {
        super("chat", aggregateId, "messageCreate", payload, "");
    }
}
