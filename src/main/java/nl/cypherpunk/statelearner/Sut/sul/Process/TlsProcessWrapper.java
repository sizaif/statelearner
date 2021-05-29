package nl.cypherpunk.statelearner.Sut.sul.Process;

import de.learnlib.api.SUL;
import de.learnlib.api.exception.SULException;
import nl.cypherpunk.statelearner.Config.SulDelegate;
import nl.cypherpunk.statelearner.Sut.io.message.TlsInput;
import nl.cypherpunk.statelearner.Sut.io.message.TlsOutput;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName TlsProcessWrapper
 * @Auther SIZ
 * @Date 2021/5/29 15:43
 **/
public class TlsProcessWrapper extends SulProcessWrapper<TlsInput, TlsOutput> {

    private StringOutputStream procOut;
    private boolean storeApplicationOutput;

    public TlsProcessWrapper(SUL<TlsInput, TlsOutput> sul,
                             SulDelegate sulDelegate) {
        super(sul, sulDelegate);
        this.storeApplicationOutput = sulDelegate.isWithApplicationOutput();
        if (storeApplicationOutput) {
            procOut = new StringOutputStream();
            handler.redirectError(procOut);
            handler.redirectOutput(procOut);
        }
    }

    public void pre() {
        super.pre();
        if (storeApplicationOutput)
            procOut.clear();
    }

    @Override
    public TlsOutput step(TlsInput in) throws SULException {
        TlsOutput output = super.step(in);
        output.setAlive(super.isAlive());
        if (storeApplicationOutput) {
            String procOutput = procOut.getString();
            output.setApplicationOutput(procOutput);
            procOut.clear();
        }
        return output;
    }

    class StringOutputStream extends OutputStream {

        private StringBuilder mBuf;

        StringOutputStream() {
            mBuf = new StringBuilder();
        }

        public String getString() {
            return mBuf.toString();
        }

        @Override
        public void write(int arg0) throws IOException {
            mBuf.append((char) arg0);
        }

        public void clear() {
            mBuf = new StringBuilder();
        }

    }
}
