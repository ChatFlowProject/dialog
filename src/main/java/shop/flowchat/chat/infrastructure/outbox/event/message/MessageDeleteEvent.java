package shop.flowchat.chat.infrastructure.outbox.event.message;

import shop.flowchat.chat.infrastructure.outbox.payload.MessageDeletePayload;
import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;

public class MessageDeleteEvent extends OutboxEvent {

    public MessageDeleteEvent(String aggregateId, MessageDeletePayload payload) {
        super("chat", aggregateId, "messageDelete", payload, "");
    }
}
