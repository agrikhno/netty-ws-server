package netty.ws.server.test.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import netty.ws.server.test.event.type.OutgoingEventType;
import netty.ws.server.test.model.OutgoingChatMessage;


@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OutgoingWebsocketEvent<T> {

    /**
     * Event type
     */
    private OutgoingEventType type;

    /**
     * Event payload
     */
    private T source;


    /**
     * json representation of this event. for optimisation purposes
     */
    private String eventTextRepresentation;


    public static OutgoingWebsocketEvent<OutgoingChatMessage> message(OutgoingChatMessage outgoingChatMessage) {
        return OutgoingWebsocketEvent.<OutgoingChatMessage>builder()
                .type(OutgoingEventType.MESSAGE)
                .source(outgoingChatMessage)
                .build();
    }
}
