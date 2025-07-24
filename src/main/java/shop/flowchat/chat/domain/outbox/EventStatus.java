package shop.flowchat.chat.domain.outbox;

import lombok.Getter;

@Getter
public enum EventStatus {
    PENDING, SUCCESS, FAILED
}
