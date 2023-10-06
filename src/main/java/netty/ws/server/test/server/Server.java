package netty.ws.server.test.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.ws.server.test.chat.ChatRoom;
import netty.ws.server.test.service.LookupService;
import netty.ws.server.test.service.impl.DefaultLookupService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static io.netty.channel.ChannelOption.SO_RCVBUF;
import static io.netty.channel.ChannelOption.SO_SNDBUF;


public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {

        final Map<String, ChatRoom> refKeyChatRoomMap = new HashMap<>();
        final AtomicLong messageCounter = new AtomicLong();

        final int roomCount = 10;

        for (int i = 1; i <= roomCount; i++) {
            refKeyChatRoomMap.put("room_" + i, new ChatRoom(new ConcurrentHashMap<>(), messageCounter));
        }

        LookupService lookupService = new DefaultLookupService(refKeyChatRoomMap);

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 2048);
            serverBootstrap.option(SO_SNDBUF, 128 * 1024);
            serverBootstrap.option(SO_RCVBUF, 64 * 1024);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HTTPInitializer(lookupService, getJackson()));

            Channel ch = serverBootstrap.bind(PORT).sync().channel();
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static ObjectMapper getJackson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}