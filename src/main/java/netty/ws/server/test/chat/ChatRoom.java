package netty.ws.server.test.chat;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ChatRoom {

    private final Map<String, UserSession> sessionMap;
    private final AtomicLong messageCounter;


    public ChatRoom(Map<String, UserSession> sessionMap, AtomicLong messageCounter) {
        this.sessionMap = sessionMap;
        this.messageCounter = messageCounter;
    }


    public void addUser(String userId, ChannelHandlerContext context, ObjectMapper jacksonMapper) {
        sessionMap.put(userId, new UserSession(context, this, messageCounter, jacksonMapper));
    }

    public UserSession getUserSessionById(String userId) {
        return sessionMap.get(userId);
    }

    public void removeUser(String userId) {
        sessionMap.remove(userId);
    }

    public Collection<UserSession> getUserSessions() {
        return sessionMap.values();
    }

}
