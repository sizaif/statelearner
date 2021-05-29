package nl.cypherpunk.statelearner.Execute;

import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.record.AbstractRecord;
import de.rub.nds.tlsattacker.core.state.State;
import de.rub.nds.tlsattacker.core.state.TlsContext;
import de.rub.nds.tlsattacker.core.workflow.action.executor.SendMessageHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ExecuteInputHelper
 * @Auther SIZ
 * @Date 2021/5/27 15:34
 **/
public class ExecuteInputHelper {

    public final void prepareMessage(ProtocolMessage message, State state){
        message.getHandler(state.getTlsContext()).prepareMessage(message);
    }


    /**
     * 将消息/片段打包成记录。
     * @param messages
     * @param state
     * @return
     */
    public final PackingResult packingResult(List<ProtocolMessage> messages, State state){

        List<AbstractRecord> records = new LinkedList<>();

        for(ProtocolMessage message:messages){
            AbstractRecord record = state.getTlsContext().getRecordLayer().getFreshRecord();
            records.add(record);
            byte[] data = message.getCompleteResultingMessage().getValue();
            state.getTlsContext().getRecordLayer().prepareRecords(data,message.getProtocolMessageType(), Collections.singletonList(record));
        }
        return new PackingResult(messages,records);
    }

    public final PackingResult packingResult(List<ProtocolMessage> messages, TlsContext state){

        List<AbstractRecord> records = new LinkedList<>();
        for (ProtocolMessage message : messages) {
            AbstractRecord record = state.getRecordLayer().getFreshRecord();
            records.add(record);
            byte[] data = message.getCompleteResultingMessage().getValue();
            state.getRecordLayer().prepareRecords(data, message.getProtocolMessageType(), Collections.singletonList(record));
        }
        return new PackingResult(messages, records);
    }

    /**
     * 通过网络发送记录
     * @param records
     * @param state
     */
    public final void sendRecords(List<AbstractRecord> records,State state){
        SendMessageHelper helper = new SendMessageHelper();
        try {
            helper.sendRecords(records,state.getTlsContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
