package netty.ws.server.test.chat;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import netty.ws.server.test.event.OutgoingWebsocketEvent;
import netty.ws.server.test.model.ChatMessage;
import netty.ws.server.test.model.OutgoingChatMessage;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class UserSession {

    private final ChannelHandlerContext context;
    private final ChatRoom chatRoom;
    private final AtomicLong messageCounter;
    private final ObjectMapper jacksonMapper;

    @SneakyThrows
    public void handleIncomingMessage(String message) {
        OutgoingWebsocketEvent<OutgoingChatMessage> outgoingWebsocketEvent = OutgoingWebsocketEvent
                .message(OutgoingChatMessage
                        .builder()
                        .message(
                                ChatMessage.builder()
                                        .id(messageCounter.incrementAndGet())
                                        .userName("UserName")
                                        .message(message)
                                        .build()
                        )
                        .build()
                );

        final String outgoingMessage = jacksonMapper.writeValueAsString(outgoingWebsocketEvent);
        outgoingWebsocketEvent.setEventTextRepresentation(outgoingMessage);

        for (UserSession userSession : chatRoom.getUserSessions()) {
            userSession.handleOutgoingMessage(outgoingWebsocketEvent);
        }
    }

    public void handleOutgoingMessage(OutgoingWebsocketEvent<OutgoingChatMessage> outgoingWebsocketEvent) {
        context.channel().writeAndFlush(outgoingWebsocketEvent);
    }

}
