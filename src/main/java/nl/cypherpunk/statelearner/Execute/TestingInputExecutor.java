package nl.cypherpunk.statelearner.Execute;

import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.state.State;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName TestingInputExecutor
 * @Auther SIZ
 * @Date 2021/5/27 15:32
 **/

/**
 * 一个具体的输入执行器，它实现规则的/不改变的方式发送消息
 *
 * 它为消息发送的每个阶段提供可重写的方法进程，并更新执行上下文。
 *
 * 这可以为所学习模型的一致性测试形成良好的基础，因为
 * 可以改变输入的执行方式，或者改变生成的消息。
 */
public class TestingInputExecutor extends AbstractInputExecutor {

    private ExecuteInputHelper helper;
    // 联系发送
    private boolean sequentialSend;

    public TestingInputExecutor() {
        helper = new ExecuteInputHelper();
        sequentialSend = false;
    }

    @Override
    protected void sendMessage(ProtocolMessage message, State state, ExecutionContext context) {
        // 准备消息
        message = prepareMessage(message, state, context);
        List<ProtocolMessage> messagesToSend = new LinkedList<>();
        //如果版本是TLS12or TLS11 版本 并且是握手消息
        if(message.isHandshakeMessage() && state.getTlsContext().getConfig().getDefaultSelectedProtocolVersion().usesExplicitIv()){
            messagesToSend.add(message);
        }else {

        }
        // 打包消息
        PackingResult result = packMessages(messagesToSend, state, context);

        //设置打包消息
        context.getStepContext().setPackingResult(result);

        message.getHandler(state.getTlsContext()).adjustTlsContextAfterSerialize(message);

        if(sequentialSend){
            result.getRecords().forEach(r -> helper.sendRecords(Arrays.asList(r),state));
        }
        else {
            helper.sendRecords(result.getRecords(),state);
        }
    }

    private PackingResult packMessages(List<ProtocolMessage> messagesToSend, State state, ExecutionContext context) {
        PackingResult result = helper.packingResult(messagesToSend,state);
        return result;
    }

    private ProtocolMessage prepareMessage(ProtocolMessage message, State state, ExecutionContext context) {
        helper.prepareMessage(message,state);
        return message;
    }
}
