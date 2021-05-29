package nl.cypherpunk.statelearner.Execute;

import sun.security.ssl.HandshakeMessage;

/**
 * @ClassName HandshakeResult
 * @Auther SIZ
 * @Date 2021/5/25 11:00
 **/
public class HandshakeResult {

    private final HandshakeMessage message;


    public HandshakeResult(HandshakeMessage message) {
        super();
        this.message = message;
    }

    public HandshakeMessage getMessage() {
        return message;
    }
}
