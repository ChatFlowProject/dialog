package shop.flowchat.chat.infrastructure.outbox.event.message;

import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MessageUpdatePayload;

public class MessageUpdateEvent extends OutboxEvent {

    public MessageUpdateEvent(String aggregateId, MessageUpdatePayload payload) {
        super("chat", aggregateId, "messageUpdate", payload, "");
    }
}
