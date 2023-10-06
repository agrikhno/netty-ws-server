package netty.ws.server.test.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.RequiredArgsConstructor;
import netty.ws.server.test.server.handlers.codec.TextWebsocketDecoder;
import netty.ws.server.test.server.handlers.codec.TextWebsocketEncoder;
import netty.ws.server.test.service.LookupService;

@RequiredArgsConstructor
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private final LookupService lookupService;
    private final ObjectMapper jacksonMapper;
    private final TextWebsocketDecoder textWebsocketDecoder;
    private final TextWebsocketEncoder textWebsocketEncoder;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof HttpRequest httpRequest) {
            final HttpHeaders headers = httpRequest.headers();

            if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION)) &&
                    "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {

                //Adding new handler to the existing pipeline to handle WebSocket Messages
                ctx.pipeline().replace(this, "websocketHandler", new WebSocketHandler(lookupService, jacksonMapper));
                ctx.pipeline().addBefore("websocketHandler", "textWebsocketDecoder", textWebsocketDecoder);
                ctx.pipeline().addAfter("websocketHandler", "textWebsocketEncoder", textWebsocketEncoder);

                //Do the Handshake to upgrade connection from HTTP to WebSocket protocol
                handleHandshake(ctx, httpRequest);
            }
        } else {
            System.out.println("Incoming request is unknown");
        }
    }

    /* Do the handshaking for WebSocket request */
    protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) {
        final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketURL(req), null, true);
        final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    protected String getWebSocketURL(HttpRequest req) {
        return "ws://" + req.headers().get("Host") + req.uri();
    }
}