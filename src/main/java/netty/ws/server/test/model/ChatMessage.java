package netty.ws.server.test.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessage {

    private Long id;

    private String userName;

    private String message;

}
