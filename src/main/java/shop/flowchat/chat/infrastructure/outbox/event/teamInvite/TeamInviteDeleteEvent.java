package shop.flowchat.chat.infrastructure.outbox.event.teamInvite;

import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.TeamInviteEventPayload;

public class TeamInviteDeleteEvent extends OutboxEvent {
    public TeamInviteDeleteEvent(String aggregateId, TeamInviteEventPayload payload) {
        super("teamInvite", aggregateId, "teamInviteDelete", payload, "");
    }
}
