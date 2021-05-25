package nl.cypherpunk.statelearner.Sut.io.message;

import de.rub.nds.tlsattacker.core.protocol.message.FinishedMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.state.State;
import nl.cypherpunk.statelearner.execute.ExecutionContext;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @ClassName FinishedInput
 * @Auther SIZ
 * @Date 2021/5/25 17:12
 **/
public class FinishedInput extends NamedTlsInput {
    @XmlAttribute(name = "resetMSeq", required = true)
    private boolean resetMSeq = false;

    public FinishedInput() {
        super("FINISHED");
    }

    @Override
    public ProtocolMessage generateMessage(State state) {
        FinishedMessage message = new FinishedMessage();
        return message;
    }

    @Override
    public TlsInputType getInputType() {
        return TlsInputType.HANDSHAKE;
    }

//    @Override
//    public void postReceiveUpdate(TlsOutput output, State state, ExecutionContext context) {
//        super.postReceiveUpdate(output, state, context);
//        state.getTlsContext().getDigest().reset();
//
//    }
}
