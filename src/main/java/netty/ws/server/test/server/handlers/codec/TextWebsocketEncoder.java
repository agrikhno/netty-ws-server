package netty.ws.server.test.server.handlers.codec;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import netty.ws.server.test.event.OutgoingWebsocketEvent;
import netty.ws.server.test.model.OutgoingChatMessage;

import java.util.List;


@Sharable
public class TextWebsocketEncoder extends MessageToMessageEncoder<OutgoingWebsocketEvent<OutgoingChatMessage>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, OutgoingWebsocketEvent<OutgoingChatMessage> outgoingWebsocketEvent, List<Object> out) {
        final String json = outgoingWebsocketEvent.getEventTextRepresentation();
        out.add(new TextWebSocketFrame(json));
    }

}
