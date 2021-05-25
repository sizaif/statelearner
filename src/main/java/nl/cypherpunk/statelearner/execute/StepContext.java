package nl.cypherpunk.statelearner.execute;

import de.rub.nds.tlsattacker.core.protocol.message.HandshakeMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.record.AbstractRecord;
import nl.cypherpunk.statelearner.Sut.io.TlsInput;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName StepContext
 * @Auther SIZ
 * @Date 2021/5/25 10:32
 **/
public class StepContext {

    //private FragmentationResult fragmentationResult;
    private HandshakeResult handshakeResult;
    private PackingResult packingResult;
    private List<ProtocolMessage> receivedOutputMessages;
    private List<HandshakeMessage> receivedHandshakeMessages;
    private List<AbstractRecord> deferredRecords;
    private TlsInput input;

    /**
     * A boolean for disabling current execution.
     */
    private boolean disabled;

    public StepContext() {
        deferredRecords = Collections.emptyList();
        disabled = false;
    }

//    public FragmentationResult getFragmentationResult() {
//        return fragmentationResult;
//    }


    public List<HandshakeMessage> getReceivedHandshakeMessages() {
        return receivedHandshakeMessages;
    }

    public List<AbstractRecord> getDeferredRecords() {
        return deferredRecords;
    }

    public List<ProtocolMessage> getReceivedOutputMessages() {
        return receivedOutputMessages;
    }

    public HandshakeResult getHandshakeResult() {
        return handshakeResult;
    }

    public void setHandshakeResult(HandshakeResult handshakeResult) {
        this.handshakeResult = handshakeResult;
    }

//    public void setFragmentationResult(FragmentationResult fragmentationResult) {
//        this.fragmentationResult = fragmentationResult;
//    }

    public void setReceivedHandshakeMessages(List<HandshakeMessage> receivedHandshakeMessages) {
        this.receivedHandshakeMessages = receivedHandshakeMessages;
    }

    public void setPackingResult(PackingResult packingResult) {
        this.packingResult = packingResult;
    }

    public void setReceivedOutputMessages(List<ProtocolMessage> receivedOutputMessages) {
        this.receivedOutputMessages = receivedOutputMessages;
    }

    public TlsInput getInput() {
        return input;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setInput(TlsInput input) {
        this.input = input;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setDeferredRecords(List<AbstractRecord> deferredRecords) {
        this.deferredRecords = deferredRecords;
    }

    public PackingResult getPackingResult() {
        return packingResult;
    }
    public void disable(){
        disabled = true;
    }
}
