package shop.flowchat.chat.infrastructure.outbox.event.mention;

import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MentionEventPayload;

public class MentionDeleteEvent extends OutboxEvent {

    public MentionDeleteEvent(String aggregateId, MentionEventPayload payload) {
        super("mention", aggregateId, "mentionDelete", payload, "");
    }

}