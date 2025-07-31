package shop.flowchat.chat.infrastructure.outbox.event.mention;

import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.MentionEventPayload;

public class MentionCreateEvent extends OutboxEvent {

    public MentionCreateEvent(String aggregateId, MentionEventPayload payload) {
        super("mention", aggregateId, "mentionCreate", payload, "");
    }

}