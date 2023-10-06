package netty.ws.server.test.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import netty.ws.server.test.event.type.IncomingEventType;
import netty.ws.server.test.model.ClientMessage;
import netty.ws.server.test.model.LoginMessage;


@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IncomingWebsocketEvent<T> {

    /**
     * Event type
     */
    private IncomingEventType type;

    /**
     * Event payload
     */
    private T event;


    public static IncomingWebsocketEvent<LoginMessage> login(LoginMessage loginMessage) {
        return IncomingWebsocketEvent.<LoginMessage>builder()
                .type(IncomingEventType.LOGIN)
                .event(loginMessage)
                .build();
    }

    public static IncomingWebsocketEvent<ClientMessage> message(ClientMessage incomingCallMessage) {
        return IncomingWebsocketEvent.<ClientMessage>builder()
                .type(IncomingEventType.MESSAGE)
                .event(incomingCallMessage)
                .build();
    }

}
