package netty.ws.server.test.service;


import netty.ws.server.test.chat.ChatRoom;

public interface LookupService {

    ChatRoom roomLookup(String roomContextKey);

}
