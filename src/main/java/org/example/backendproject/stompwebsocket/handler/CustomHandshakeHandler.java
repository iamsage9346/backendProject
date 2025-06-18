package org.example.backendproject.stompwebsocket.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        String nickname = getNickName(request.getURI().getQuery());
        return new StompPrincipal(nickname);
    }

    private String getNickName(String query) {
        if (query == null || query.contains("nickname=")) {
            return "no nickname";
        } else {
            return query.split("nickname=")[1];
        }
    }
}
