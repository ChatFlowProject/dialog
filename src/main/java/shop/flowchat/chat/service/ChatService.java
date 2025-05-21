package shop.flowchat.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.chat.dto.chat.response.ChatCreateResponse;
import shop.flowchat.chat.entity.Chat;
import shop.flowchat.chat.repository.ChatRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    @Transactional
    public ChatCreateResponse createChat() {
        Chat chat = Chat.builder().build();
        chatRepository.save(chat);
        return new ChatCreateResponse(chat.getId());
    }
}
