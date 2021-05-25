package nl.cypherpunk.statelearner.Sut.io.message;

import de.rub.nds.tlsattacker.core.constants.CipherSuite;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.state.State;
import nl.cypherpunk.statelearner.execute.ExecutionContext;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @ClassName ClientHelloRenegotiationInput
 * @Auther SIZ
 * @Date 2021/5/25 16:57
 **/
public class ClientHelloRenegotiationInput extends NamedTlsInput{
    static enum Enabled {
        OWN_EPOCH_CHANGE, SERVER_EPOCH_CHANGE, ALWAYS, ONCE, ON_SERVER_HELLO
    }
    @XmlAttribute(name = "short", required = false)
    private boolean isShort = false;

    @XmlAttribute(name = "suite", required = false)
    private CipherSuite suite = null;

    @XmlAttribute(name = "enabled", required = false)
    private Enabled enabled = Enabled.ALWAYS;

    @XmlAttribute(name = "withCookie", required = false)
    private boolean withCookie = true;

    @XmlAttribute(name = "resetMSeq", required = false)
    private boolean resetMSeq = true;

    public ClientHelloRenegotiationInput() {
        super("CLIENT_HELLO_RENEGOTIATION");
    }

    public boolean isEnabled(State state, ExecutionContext context) {
        switch (enabled){
            case ONCE:
                return context.getStepContextList().subList(0,context.getStepCount()-1).stream()
                        .noneMatch(s ->s.getInput().equals(this));
            case OWN_EPOCH_CHANGE:
            default:
                return false;
        }
    }

    @Override
    public ProtocolMessage generateMessage(State state) {
        state.getTlsContext().getDigest().reset();
        if(resetMSeq){

        }
        return null;
    }

    @Override
    public TlsInputType getInputType() {
        return TlsInputType.HANDSHAKE;
    }


}
