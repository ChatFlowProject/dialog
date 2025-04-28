//package shop.flowchat.chat.controller;
//
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.web.bind.annotation.RestController;
//import shop.flowchat.chat.dto.message.request.MessageCreateRequest;
//
//@RestController
//public class MessageController {
//
//    @MessageMapping("/message/{chatRoomId}")
//    public void sendMessage(@DestinationVariable Long chatRoomId, MessageCreateRequest request) {
//        System.out.println("채팅 룸 ID " + chatRoomId);
//        System.out.println("메시지 : " + request);
//        if (request.attachments() != null) {
//            request.attachments().forEach(att -> {
//                System.out.println("첨부파일 타입: " + att.type());
//                System.out.println("첨부파일 URL: " + att.url());
//            });
//        }
//    }
//}
