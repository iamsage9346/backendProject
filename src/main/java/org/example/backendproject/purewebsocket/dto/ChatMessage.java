package org.example.backendproject.purewebsocket.dto;

import lombok.Getter;

@Getter
public class ChatMessage {
    private String message;
    private String from;
    private String roomId;
}
