package nl.cypherpunk.statelearner.Sut.sul;

import de.learnlib.api.SUL;
import de.learnlib.api.exception.SULException;
import nl.cypherpunk.statelearner.CleanupTasks;
import nl.cypherpunk.statelearner.Config.SulDelegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * @ClassName ResettingWrapper
 * @Auther SIZ
 * @Date 2021/5/29 15:59
 **/
public class ResettingWrapper<I, O> implements SUL<I, O>, DynamicPortProvider {
    private SUL<I, O> sul;

    private Socket resetSocket;
    private InetSocketAddress resetAddress;
    private long resetCommandWait;
    private Integer dynamicPort;
    private BufferedReader reader;

    public ResettingWrapper(SUL<I, O> sul, SulDelegate sulDelegate,
                            CleanupTasks tasks) {
        this.sul = sul;
        resetAddress = new InetSocketAddress(sulDelegate.getResetAddress(),
                sulDelegate.getResetPort());
        resetCommandWait = sulDelegate.getResetCommandWait();
        try {
            resetSocket = new Socket();
            resetSocket.setReuseAddress(true);
            resetSocket.setSoTimeout(20000);
            tasks.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!resetSocket.isClosed()) {
                            System.out.println("Closing socket");
                            resetSocket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getSulPort() {
        return dynamicPort;
    }

    @Override
    public void pre() {
        try {
            if (!resetSocket.isConnected()) {
                resetSocket.connect(resetAddress);
                reader = new BufferedReader(new InputStreamReader(
                        resetSocket.getInputStream()));
            }
            byte[] resetCmd = "reset\n".getBytes();

            resetSocket.getOutputStream().write(resetCmd);
            resetSocket.getOutputStream().flush();
            String portString = reader.readLine();
            if (portString == null) {
                throw new RuntimeException("Server has closed the socket");
            }
            dynamicPort = Integer.valueOf(portString);
            if (resetCommandWait > 0)
                Thread.sleep(resetCommandWait);

            /*
             * We have to pre before the SUT does, so we have a port available
             * for it.
             */

            sul.pre();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void post() {
        sul.post();
    }

    @Override
    public O step(I in) throws SULException {
        return sul.step(in);
    }
}
