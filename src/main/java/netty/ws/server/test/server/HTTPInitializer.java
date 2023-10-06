package netty.ws.server.test.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import netty.ws.server.test.server.handlers.HttpServerHandler;
import netty.ws.server.test.server.handlers.codec.TextWebsocketDecoder;
import netty.ws.server.test.server.handlers.codec.TextWebsocketEncoder;
import netty.ws.server.test.service.LookupService;


public class HTTPInitializer extends ChannelInitializer<SocketChannel> {

    private final LookupService lookupService;
    private final ObjectMapper jacksonMapper;
    private final TextWebsocketDecoder textWebsocketDecoder;
    private final TextWebsocketEncoder textWebsocketEncoder;

    public HTTPInitializer(LookupService lookupService, ObjectMapper jacksonMapper) {
        this.lookupService = lookupService;
        this.jacksonMapper = jacksonMapper;
        this.textWebsocketDecoder = new TextWebsocketDecoder(jacksonMapper);
        this.textWebsocketEncoder = new TextWebsocketEncoder();
    }

    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpHandler", new HttpServerHandler(lookupService, jacksonMapper, textWebsocketDecoder, textWebsocketEncoder));
    }
}