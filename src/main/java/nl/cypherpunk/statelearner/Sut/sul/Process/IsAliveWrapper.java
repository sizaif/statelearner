package nl.cypherpunk.statelearner.Sut.sul.Process;

import de.learnlib.api.SUL;
import de.learnlib.api.exception.SULException;
import nl.cypherpunk.statelearner.Sut.io.message.TlsInput;
import nl.cypherpunk.statelearner.Sut.io.message.TlsOutput;

/**
 * @ClassName IsAliveWrapper
 * @Auther SIZ
 * @Date 2021/5/29 16:01
 **/
public class IsAliveWrapper implements SUL<TlsInput, TlsOutput> {
    private SUL<TlsInput, TlsOutput> sut;
    private boolean isAlive;

    public IsAliveWrapper(SUL<TlsInput, TlsOutput> tlsSut) {
        this.sut = sut;
    }
    @Override
    public void pre() {
        sut.pre();
        isAlive = true;
    }

    @Override
    public void post() {
        sut.post();
    }

    @Override
    public TlsOutput step(TlsInput in) throws SULException {
        if (isAlive) {
            TlsOutput out = sut.step(in);
            isAlive = out.isAlive();
            return out;
        } else {
            return TlsOutput.socketClosed();
        }
    }
}
