package nl.cypherpunk.statelearner.Sut.sul.Process;

import de.learnlib.api.SUL;
import de.learnlib.api.exception.SULException;
import nl.cypherpunk.statelearner.Config.SulDelegate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName SulProcessWrapper
 * @Auther SIZ
 * @Date 2021/5/29 15:44
 **/

/**
 * 负责启动/终止进程的sul包装器
 * 登陆可以在两个不同的触发点进行:
 * 1.一旦在开始，在学习/测试结束时终止;
 * 2 在执行每个测试之前，在测试完成后终止执行。
 *
 */
public class SulProcessWrapper<I, O> implements SUL<I,O> {
    private static Map<String, ProcessHandler> handlers = new LinkedHashMap<>();

    protected ProcessHandler handler = null;

    private SUL<I, O> sul;
    // TODO having the trigger here is not nice since it limits the trigger
    // options. Ideally we would have it outside.
    private ProcessLaunchTrigger trigger;

    //我们应该传递ProcessConfig类，处理程序变成一个映射
    //从ProcessConfig到proceshandler。
    public SulProcessWrapper(SUL<I, O> sul, SulDelegate sulDelegate) {
        this.sul = sul;
        if (!handlers.containsKey(sulDelegate.getCommand()))
            handlers.put(sulDelegate.getCommand(), new ProcessHandler(
                    sulDelegate));
        this.handler = handlers.get(sulDelegate.getCommand());
        this.trigger = sulDelegate.getProcessTrigger();
        if (trigger == ProcessLaunchTrigger.START && !handler.hasLaunched()) {
            handler.launchProcess();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.terminateProcess();
                }

            }));
        }
    }
    @Override
    public void pre() {
        sul.pre();
        if (trigger == ProcessLaunchTrigger.NEW_TEST) {
            handler.launchProcess();
        }
    }

    @Override
    public void post() {
        sul.post();
        if (trigger == ProcessLaunchTrigger.NEW_TEST) {
            handler.terminateProcess();
        }
    }

    @Override
    public O step(I in) throws SULException {
        return sul.step(in);
    }

    public boolean isAlive() {
        return handler.isAlive();
    }
}
