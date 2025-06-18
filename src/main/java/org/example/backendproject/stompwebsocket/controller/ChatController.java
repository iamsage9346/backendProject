package org.example.backendproject.stompwebsocket.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendproject.stompwebsocket.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        if (message.getTo() != null && !message.getTo().isEmpty()) {
            // 귓속말 처리 (to 값이 있으면)
            messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/private", message);
        } else {
            // 공개 채팅 처리
            messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
        }
    }

}
