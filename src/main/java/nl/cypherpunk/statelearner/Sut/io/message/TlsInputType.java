package nl.cypherpunk.statelearner.Sut.io.message;

import de.rub.nds.tlsattacker.core.constants.ProtocolMessageType;

/**
 * @ClassName TlsInputType
 * @Auther SIZ
 * @Date 2021/5/25 11:24
 **/
public enum  TlsInputType {
    CCS,HANDSHAKE, ALERT, APPLICATION, UNKNOWN, HEARTBEAT;
    public static TlsInputType fromProtocolMessageType(ProtocolMessageType type){
        switch (type){
            case ALERT:
                return TlsInputType.ALERT;
            case APPLICATION_DATA:
                return TlsInputType.APPLICATION;
            case UNKNOWN:
                return TlsInputType.UNKNOWN;
            case HANDSHAKE:
                return TlsInputType.HANDSHAKE;
            case HEARTBEAT:
                return TlsInputType.HEARTBEAT;
            default:
                throw new RuntimeException("type not supported: "+type);
        }
    }
}
