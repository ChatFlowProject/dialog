package shop.flowchat.chat.infrastructure.security;

import java.security.Principal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class StompPrincipal implements Principal {

    private final UUID id;
    private final String name;
    private final String avatarUrl;

    public StompPrincipal(UUID id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String getName() {
        return name;
    }
}