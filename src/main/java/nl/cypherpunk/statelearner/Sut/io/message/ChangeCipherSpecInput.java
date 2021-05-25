package nl.cypherpunk.statelearner.Sut.io.message;

import de.rub.nds.tlsattacker.core.protocol.message.ChangeCipherSpecMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.state.State;
import nl.cypherpunk.statelearner.execute.ExecutionContext;

/**
 * @ClassName ChangeCipherSpecInput
 * @Auther SIZ
 * @Date 2021/5/25 15:04
 **/
public class ChangeCipherSpecInput extends NamedTlsInput{

    @Override
    public ProtocolMessage generateMessage(State state) {
        ChangeCipherSpecMessage ccs = new ChangeCipherSpecMessage(state.getConfig());
        return ccs;
    }

    @Override
    public void postSendUpdate(State state, ExecutionContext context) {
        state.getTlsContext().getRecordLayer().updateEncryptionCipher();
        state.getTlsContext().setWriteSequenceNumber(0);
    }
    @Override
    public TlsInputType getInputType() {
        return TlsInputType.CCS;
    }
}
