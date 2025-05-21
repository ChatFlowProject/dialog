package shop.flowchat.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.flowchat.chat.dto.chat.response.ChatCreateResponse;
import shop.flowchat.chat.entity.Chat;
import shop.flowchat.chat.repository.ChatRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatCreateResponse createChat() {
        Chat chat = Chat.create();
        chatRepository.save(chat);
        return new ChatCreateResponse(chat.getId());
    }
}
