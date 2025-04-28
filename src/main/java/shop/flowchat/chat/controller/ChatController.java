package shop.flowchat.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.common.ApiResponse;
import shop.flowchat.chat.dto.message.request.MessageCreateRequest;
import shop.flowchat.chat.dto.message.response.MessageCreateResponse;
import shop.flowchat.chat.dto.message.response.MessagePushResponse;
import shop.flowchat.chat.service.ChatService;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/{chatRoomId}/messages") // Todo: 채팅을 웹소켓으로 보내줄지 http로 보내주는지 물어보기(노션 기준으로는 http)
    public ApiResponse<MessageCreateResponse> sendMessage(@PathVariable Long chatRoomId, @RequestBody MessageCreateRequest request) {
        MessageCreateResponse response = chatService.saveMessage(chatRoomId, request);
        MessagePushResponse message = MessagePushResponse.of(chatRoomId, request);
        messagingTemplate.convertAndSend("/sub/message/" + chatRoomId, message);
        return ApiResponse.success(response);
    }
}
