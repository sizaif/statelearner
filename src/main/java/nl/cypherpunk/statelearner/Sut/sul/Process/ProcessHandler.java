package nl.cypherpunk.statelearner.Sut.sul.Process;

/**
 * @ClassName ProcessHandler
 * @Auther SIZ
 * @Date 2021/5/29 15:48
 **/

import nl.cypherpunk.statelearner.Config.SulDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Scanner;

/**
 * 允许通过执行给定的命令启动/停止进程
 * 一次最多可以执行一个进程。
 */
public class ProcessHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ProcessBuilder pb;
    private Process currentProcess;
    private String terminateCommand;
    private OutputStream output;
    private OutputStream error;
    private long runWait;
    private boolean hasLaunched;

    private ProcessHandler(String command, long runWait) {
        // '+' after \\s takes care of multiple consecutive spaces so that they
        // don't result in empty arguments
        pb = new ProcessBuilder(command.split("\\s+"));
        this.runWait = runWait;
        output = System.out;
        error = System.err;
    }
    public ProcessHandler(SulDelegate sulConfig) {
        this(sulConfig.getCommand(), sulConfig.getRunWait());
        if (sulConfig.getProcessDir() != null) {
            setDirectory(new File(sulConfig.getProcessDir()));
        }
        terminateCommand = sulConfig.getTerminateCommand();
    }

    private void setDirectory(File file) {
        pb.directory(file);
    }
    public void redirectOutput(OutputStream toOutput) {
        output = toOutput;
    }

    public void redirectError(OutputStream toOutput) {
        error = toOutput;
    }

    /**
     * Launches a process which executes the handler's command. Does nothing if
     * the process has been launched already.
     * 启动一个执行处理程序命令的进程  如果已经启动什么也不做
     *
     * Sets {@link ProcessHandler#hasLaunched} to true on successful launch of
     * the process, making {@link ProcessHandler#hasLaunched()} return true
     * thereafter.
     *
     * After launching, it sleeps for {@link ProcessHandler#runWait}
     * milliseconds.
     */
    public void launchProcess(){
        try {
            if (currentProcess == null) {
                hasLaunched = true;
                currentProcess = pb.start();
                if (output != null)
                    inheritIO(currentProcess.getInputStream(), new PrintStream(
                            output));
                if (error != null)
                    inheritIO(currentProcess.getErrorStream(), new PrintStream(
                            error));
                if (runWait > 0)
                    Thread.sleep(runWait);
            } else {
                LOGGER.warn("Process has already been started");
            }

        } catch (IOException | InterruptedException E) {
            LOGGER.error("Couldn't start process due to exec:", E);
        }
    }
    /**
     * Terminates the process executing the handler's command. Does nothing if
     * the process has been terminated already.
     * 终止执行处理程序命令的进程 , 如果已经被终止,什么也不做
     */
    public void terminateProcess() {
        if (currentProcess != null) {
            if (terminateCommand != null) {
                try {
                    Runtime.getRuntime().exec(terminateCommand);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                currentProcess.destroyForcibly();
            }
            currentProcess = null;
        } else {
            LOGGER.warn("Process has already been ended");
        }
    }

    public boolean isAlive() {
        return currentProcess != null && currentProcess.isAlive();
    }
    /**
     * Returns true if the process has been launched successfully at least once,
     * irrespective of whether it has terminated since first execution.
     * 如果流程至少成功启动了一次，则返回true，
     * 不论首次执行后是否已终止。
     */
    public boolean hasLaunched() {
        return hasLaunched;
    }
    private void inheritIO(final InputStream src, final PrintStream dest) {
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(src);
                while (sc.hasNextLine()) {
                    dest.println(sc.nextLine());
                }
                sc.close();
            }
        }).start();
    }
}
