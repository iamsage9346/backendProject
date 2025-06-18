package org.example.backendproject.purewebsocket.room.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendproject.purewebsocket.room.entity.ChatRoom;
import org.example.backendproject.purewebsocket.room.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/rooms")
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public List<ChatRoom> getAllRooms(String roomId) {
        return roomService.findAllRooms();
    }

    @PostMapping("/{roomId}")
    public ChatRoom createRoom(@PathVariable String roomId) {
        return roomService.createRoom(roomId);
    }
}
