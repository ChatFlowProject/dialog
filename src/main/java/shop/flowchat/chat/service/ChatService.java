package shop.flowchat.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.flowchat.chat.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
}
