package nl.cypherpunk.statelearner.Sut.sul;

import de.learnlib.api.SUL;
import de.learnlib.api.exception.SULException;
import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.connection.OutboundConnection;
import de.rub.nds.tlsattacker.core.record.layer.TlsRecordLayer;
import de.rub.nds.tlsattacker.core.state.State;
import de.rub.nds.tlsattacker.transport.tcp.ClientTcpTransportHandler;
import nl.cypherpunk.statelearner.Config.SulDelegate;
import nl.cypherpunk.statelearner.Execute.AbstractInputExecutor;
import nl.cypherpunk.statelearner.Execute.ExecutionContext;
import nl.cypherpunk.statelearner.Sut.io.message.TlsInput;
import nl.cypherpunk.statelearner.Sut.io.message.TlsOutput;
import nl.cypherpunk.statelearner.TLSFuzzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @ClassName TlsSUL
 * @Auther SIZ
 * @Date 2021/5/26 22:56
 **/
/*
    sul : system under learning
    sut : system under test
 */
public class TlsSUL implements SUL<TlsInput, TlsOutput> {
    private static final Logger LOGGER = LogManager.getLogger(TlsSUL.class);
    private State state = null;
    private ExecutionContext context = null;
    private Config config;

    /**
     * the sut is closed if it has crashed resulting in IMCP packets, or it simply terminated the connection
     * 如果sut崩溃导致了IMCP数据包，或者它仅仅终止了连接，则关闭它
     */
    private boolean closed = false;
    /**
     * the sut is disabled if an input has disabled it as a result of a learning purpose
     * 如果某个输入由于学习目的而禁用了sut，则禁用它
     */
    private boolean disabled = false;
    private long resetWait = 0;
    private int count = 0;

    private SulDelegate delegate;
    private AbstractInputExecutor defaultExecutor;

    private DynamicPortProvider portProvider;


    public TlsSUL(SulDelegate delegate,AbstractInputExecutor defaultExecutor){
        this.delegate = delegate;
        this.defaultExecutor = defaultExecutor;
    }
    public void setDynamicPortProvider(DynamicPortProvider portProvider) {
        this.portProvider = portProvider;
    }

    //setup SUL.
    @Override
    public void pre() {
        Config config = getNewSulConfig(delegate);
        delegate.applyDelegate(config);

        state = new State(config);
        state.getTlsContext().setRecordLayer(new TlsRecordLayer(state.getTlsContext()));
        state.getTlsContext().setTransportHandler(null);

        // set Protocol version
        config.setHighestProtocolVersion(delegate.getProtocolVersion());
        config.setDefaultSelectedProtocolVersion(delegate.getProtocolVersion());
        OutboundConnection connection = state.getConfig().getDefaultClientConnection();

        // set port
        if(portProvider != null){
            connection.setPort(portProvider.getSulPort());
        }

        // use TLS 11 or TLS12
        if(delegate.getProtocolVersion().usesExplicitIv()){
            state.getTlsContext().setTransportHandler(
                    new ClientTcpTransportHandler(state.getConfig().getDefaultClientConnection())
            );
        }else if(delegate.getProtocolVersion().isTLS13()){

        }

        state.getTlsContext().initTransportHandler();
        state.getTlsContext().initRecordLayer();
        closed = false;
        resetWait = delegate.getResetWait();
        context = new ExecutionContext();
        disabled = false;
        LOGGER.error("Start " + count++);
    }

    //shut down SUL.
    @Override
    public void post() {
        try {
            state.getTlsContext().getTransportHandler().closeConnection();
            if (resetWait > 0) {
                Thread.sleep(resetWait);
            }
        } catch (IOException ex) {
            LOGGER.error("Could not close connections");
            LOGGER.error(ex, null);
        } catch (InterruptedException ex) {
            LOGGER.error("Could not sleep thread");
            LOGGER.error(ex, null);
        }
    }


    //make one step on the SUL.
    @Override
    public TlsOutput step(TlsInput tlsInput) throws SULException {
        context.addStepContext();
        AbstractInputExecutor executor = tlsInput.getPreferredExecutor();
        if(executor == null){
            executor = defaultExecutor;
        }
        if(disabled){
            return TlsOutput.disabled();
        }
        TlsOutput output = null;
        try{
            if(state == null){
                throw  new RuntimeException("TLS-Attacker state is not initialized");
            }else if(state.getTlsContext().getTransportHandler().isClosed() || closed){
                closed = true;
                return TlsOutput.socketClosed();
            }
            output = executeInput(tlsInput,executor);

            if (output == TlsOutput.disabled() || context.getStepContext().isDisabled()) {
                // this should lead to a disabled sink state
                disabled = true;
            }

            if (state.getTlsContext().isReceivedTransportHandlerException()) {
                closed = true;
            }
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
            return TlsOutput.socketClosed();
        }
    }
    private TlsOutput executeInput(TlsInput in, AbstractInputExecutor executor) {
        LOGGER.debug("sent:" + in.toString());
        state.getTlsContext().setTalkingConnectionEndType(state.getTlsContext().getChooser().getConnectionEndType());
        long originalTimeout = state.getTlsContext().getTransportHandler().getTimeout();
        if(in.getExtendedWait() != null){
            state.getTlsContext().getTransportHandler().setTimeout(originalTimeout+in.getExtendedWait());
        }
        TlsOutput output = executor.execute(in,state,context);

        // TODO this is a hack to get learning Pion to work even when
        // retransmissions are allowed
        if (delegate.getRepeatingOutputs() != null) {
            String header = output.getMessageHeader();
            for (String outputString : delegate.getRepeatingOutputs()) {
                header = output.getMessageHeader().replaceAll(
                        outputString + "\\+?", outputString + "\\+");
            }
            output = new TlsOutput(header);
        }

        LOGGER.debug("received:" + output);
        state.getTlsContext().getTransportHandler().setTimeout(originalTimeout);
        return output;
    }
    private Config getNewSulConfig(SulDelegate delegate) {
        if (config == null) {
            try {
                config = Config.createConfig(delegate.getSulConfigInputStream());
            } catch (IOException e) {
                throw new RuntimeException("Could not load configuration file");
            }
            delegate.applyDelegate(config);
        }
        return config.createCopy();
    }
}
