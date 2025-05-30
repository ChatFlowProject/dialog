package shop.flowchat.chat.dto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum Direction {
    UP("up"),
    DOWN("down");

    private final String direction;

    Direction(String direction) {
        this.direction = direction;
    }

    @JsonValue
    public String getDirection() {
        return direction;
    }

    @JsonCreator
    public static Direction from(String value) {
        for (Direction d : Direction.values()) {
            if (d.direction.equalsIgnoreCase(value)) {
                return d;
            }
        }
        throw new IllegalArgumentException("올바르지 않은 direction 값입니다: " + value);
    }
}
