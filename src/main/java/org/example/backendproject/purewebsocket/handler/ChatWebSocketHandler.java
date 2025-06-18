package org.example.backendproject.purewebsocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backendproject.purewebsocket.dto.ChatMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 클라이언트 세션 발급 + 동시성 해결
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    // 클라이언트가 웹소켓 서버에 접속했을 때 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        sessions.add(session);

        System.out.println("접속된 클라이언트 세션 ID = " + session.getId());
    }

    // 클라이언트가 보낸 메시지를 서버가 받았을 때 호출
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        String roomId = chatMessage.getRoomId();

        if (!rooms.containsKey(roomId)) {
            rooms.put(roomId, ConcurrentHashMap.newKeySet());
        }
        rooms.get(roomId).add(session);


        for (WebSocketSession s : rooms.get(roomId)) {
            if (s.isOpen()) {
                // 자바 객체 -> json
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                System.out.println("전송된 메시지 = " + chatMessage.getMessage());
            }
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        sessions.remove(session);

        //연결이 해제되면 소속되어있는 방에서 제거
        for (Set<WebSocketSession> room : rooms.values()){
            room.remove(session);
        }
    }
}