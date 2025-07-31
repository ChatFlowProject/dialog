package shop.flowchat.chat.infrastructure.outbox.event.teamInvite;

import shop.flowchat.chat.infrastructure.outbox.event.OutboxEvent;
import shop.flowchat.chat.infrastructure.outbox.payload.TeamInviteEventPayload;

public class TeamInviteCreateEvent extends OutboxEvent {
    public TeamInviteCreateEvent(String aggregateId, TeamInviteEventPayload payload) {
        super("teamInvite", aggregateId, "teamInviteCreate", payload, "");
    }
}
