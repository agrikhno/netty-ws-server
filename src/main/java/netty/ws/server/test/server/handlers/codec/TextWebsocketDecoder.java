package netty.ws.server.test.server.handlers.codec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import netty.ws.server.test.event.IncomingWebsocketEvent;
import netty.ws.server.test.event.type.IncomingEventType;
import netty.ws.server.test.model.ClientMessage;
import netty.ws.server.test.model.LoginMessage;

import java.util.List;


@Sharable
@RequiredArgsConstructor
public class TextWebsocketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    private final ObjectMapper jackson;

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame frame, List<Object> out) throws Exception {
        final String json = frame.text();

        final String eventType = jackson.readTree(json).get("type").asText();
        final JsonNode payload = jackson.readTree(json).get("source");
        switch (IncomingEventType.valueOf(eventType)) {
            case LOGIN ->
                    out.add(IncomingWebsocketEvent.login(jackson.treeToValue(payload, LoginMessage.class)));
            case MESSAGE ->
                    out.add(IncomingWebsocketEvent.message(jackson.treeToValue(payload, ClientMessage.class)));
        }
    }

}
