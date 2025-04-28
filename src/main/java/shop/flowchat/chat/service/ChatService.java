package shop.flowchat.chat.service;

import org.springframework.stereotype.Service;
import shop.flowchat.chat.dto.message.request.MessageCreateRequest;
import shop.flowchat.chat.dto.message.response.MessageCreateResponse;

@Service
public class ChatService {
    public MessageCreateResponse saveMessage(Long chatRoomId, MessageCreateRequest request) {
        // Todo: db에 저장과정 처리
        return new MessageCreateResponse(
            chatRoomId,
            1L,
            "승은",
            request.message(),
            request.attachments(),
            java.time.LocalDateTime.now().toString(),
            "sent"
        );
    }
}
