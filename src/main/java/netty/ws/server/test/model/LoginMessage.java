package netty.ws.server.test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginMessage {

    private String userId;
    private String roomId;

}
