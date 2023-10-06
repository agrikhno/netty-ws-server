package netty.ws.server.test.service.impl;


import netty.ws.server.test.chat.ChatRoom;
import netty.ws.server.test.service.LookupService;

import java.util.Map;

public class DefaultLookupService implements LookupService {
    private final Map<String, ChatRoom> refKeyChatRoomMap;

    public DefaultLookupService(Map<String, ChatRoom> refKeyChatRoomMap) {
        this.refKeyChatRoomMap = refKeyChatRoomMap;
    }

    @Override
    public ChatRoom roomLookup(String roomContextKey) {
        return refKeyChatRoomMap.get(roomContextKey);
    }

}
