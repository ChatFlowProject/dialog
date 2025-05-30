package shop.flowchat.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.common.ApiResponse;
import shop.flowchat.chat.dto.message.request.MessageCacheRequest;
import shop.flowchat.chat.service.MessageService;

@RestController
@RequestMapping("/message")
@Tag(name = "메시지 API")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메시지 캐싱")
    @PostMapping("/cache")
    public ApiResponse<Void> cacheMessagesBatch(@RequestBody MessageCacheRequest request) {
        messageService.cacheMessagesByUser(request);
        return ApiResponse.success(null);
    }
}