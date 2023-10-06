package netty.ws.server.test.service.impl;

import java.util.concurrent.atomic.AtomicLong;

public class IdGeneratorService {
    private final AtomicLong chatMessageId = new AtomicLong();

    public Long generateChatMessageId() {
        return chatMessageId.incrementAndGet();
    }

}
