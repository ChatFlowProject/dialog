package shop.flowchat.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ChatType {
    DM("DM 채팅"),
    CHANNEL("채널");

    private String name;
}