package shop.flowchat.chat.infrastructure.outbox.event.message;

import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MentionCreatePayload;

public class MentionCreateEvent extends OutboxEvent {

    public MentionCreateEvent(String aggregateId, MentionCreatePayload payload) {
        super("mention", aggregateId, "mentionCreate", payload, "");
    }

}