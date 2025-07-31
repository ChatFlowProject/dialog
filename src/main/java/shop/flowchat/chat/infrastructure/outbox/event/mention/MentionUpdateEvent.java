package shop.flowchat.chat.infrastructure.outbox.event.mention;

import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MentionEventPayload;

public class MentionUpdateEvent extends OutboxEvent {

    public MentionUpdateEvent(String aggregateId, MentionEventPayload payload) {
        super("mention", aggregateId, "mentionUpdate", payload, "");
    }

}