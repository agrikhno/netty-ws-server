package netty.ws.server.test.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class OutgoingChatMessage {

    private ChatMessage message;

}
