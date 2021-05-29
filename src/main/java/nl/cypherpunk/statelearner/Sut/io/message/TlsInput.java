package nl.cypherpunk.statelearner.Sut.io.message;

/**
 * @ClassName TlsInput
 * @Auther SIZ
 * @Date 2021/5/25 10:07
 **/


import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.state.State;
import nl.cypherpunk.statelearner.Execute.AbstractInputExecutor;
import nl.cypherpunk.statelearner.Execute.ExecutionContext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A TlsInput corresponds to an input in the learning alphabet. The class
 */

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class TlsInput {

    @XmlTransient
    private AbstractInputExecutor preferredExecutor = null;

    @XmlAttribute(name = "extendedWait", required = false)
    private Integer extendedWait;

    protected TlsInput() {
    }

    /**
     * Returns the preferred executor for this input, or null, if there isn't
     * one.
     */
    public AbstractInputExecutor getPreferredExecutor() {
        return preferredExecutor;
    }

    public void setPreferredExecutor(AbstractInputExecutor preferredExecutor) {
        this.preferredExecutor = preferredExecutor;
    }

    /**
     * Enables the input for execution.
     */
    public boolean isEnabled(State state, ExecutionContext context) {
        return true;
    }

    /**
     * Generates a fresh prepared message and updates the context.
     */
    public abstract ProtocolMessage generateMessage(State state);

    /**
     * Updates the context after sending the input.
     */
    public void postSendUpdate(State state, ExecutionContext context) {
    }

    /**
     * @return additional wait time before concluding a timeout (i.e. that the
     *         SUT has produced no response)
     */
    public Integer getExtendedWait() {
        return extendedWait;
    }

    /**
     * Updates the context after receiving an output.
     */
    public void postReceiveUpdate(TlsOutput output, State state,
                                  ExecutionContext context) {
    }

    /**
     * Generates an input string which is assumed to uniquely identify the
     * input.
     */
    public abstract String toString();

    /**
     * The type of the input should correspond to the type of the message the
     * input generates.
     */
    public abstract TlsInputType getInputType();
}
