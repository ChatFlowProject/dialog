package shop.flowchat.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.flowchat.chat.dto.common.ApiResponse;
import shop.flowchat.chat.dto.common.Direction;
import shop.flowchat.chat.dto.message.request.MessageCacheRequest;
import shop.flowchat.chat.dto.message.response.MessageResponse;
import shop.flowchat.chat.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
@Tag(name = "메시지 API")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "방향에 따라 기준 메시지 ID로 메시지 페이지 조회")
    @GetMapping("/list")
    public ApiResponse<List<MessageResponse>> getMessages(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam UUID chatId,
            @RequestParam(required = false) Long messageId,
            @RequestParam(defaultValue = "up") String direction,
            @RequestParam(defaultValue = "30") int pageSize
    ) {
        Direction dir = Direction.from(direction);
        return ApiResponse.success(messageService.getMessagesByDirection(token, chatId, messageId, dir, pageSize));
    }

    @Operation(summary = "메시지 캐싱")
    @PostMapping("/cache")
    public ApiResponse<Void> cacheMessagesBatch(@RequestBody MessageCacheRequest request) {
        messageService.cacheMessagesByUser(request);
        return ApiResponse.success(null);
    }
}