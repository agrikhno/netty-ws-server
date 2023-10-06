package netty.ws.server.test.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import netty.ws.server.test.chat.ChatRoom;
import netty.ws.server.test.chat.UserSession;
import netty.ws.server.test.event.IncomingWebsocketEvent;
import netty.ws.server.test.model.ClientMessage;
import netty.ws.server.test.model.LoginMessage;
import netty.ws.server.test.service.LookupService;


public class WebSocketHandler extends SimpleChannelInboundHandler<IncomingWebsocketEvent<?>> {

    private final LookupService lookupService;
    private final ObjectMapper jacksonMapper;
    public static final AttributeKey<String> ROOM_NAME_ATTR = AttributeKey.newInstance("ROOM_NAME_ATTR");

    public WebSocketHandler(LookupService lookupService, ObjectMapper jacksonMapper) {
        super();
        this.lookupService = lookupService;
        this.jacksonMapper = jacksonMapper;
    }


    /**
     * Is called for each message of type {@link IncomingWebsocketEvent<>}.
     *
     * @param context the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param incomingWebsocketEvent the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, IncomingWebsocketEvent incomingWebsocketEvent) {
        switch (incomingWebsocketEvent.getType()) {
            case LOGIN ->
                    handleLogin(context, (LoginMessage) incomingWebsocketEvent.getEvent());
            case MESSAGE ->
                    handleIncomingMessage(context, (ClientMessage) incomingWebsocketEvent.getEvent());
        }
    }

    private void handleIncomingMessage(ChannelHandlerContext context, ClientMessage clientMessage) {
        final String room = context.channel().attr(ROOM_NAME_ATTR).get();
        final ChatRoom chatRoom = lookupService.roomLookup(room);

        final UserSession userSession = chatRoom.getUserSessionById(clientMessage.getId());
        if (null != userSession) {
            userSession.handleIncomingMessage(clientMessage.getMsg());
        }
    }


    private void handleLogin(ChannelHandlerContext context, LoginMessage loginMessage) {
        final String roomName = loginMessage.getRoomId();
        final String userId = loginMessage.getUserId();

        context.channel().attr(ROOM_NAME_ATTR).set(roomName);

        ChatRoom chatRoom = lookupService.roomLookup(roomName);
        chatRoom.addUser(userId, context, jacksonMapper);
    }
}
