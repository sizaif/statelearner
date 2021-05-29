package nl.cypherpunk.statelearner.Execute;

/**
 * @ClassName Packingresult
 * @Auther SIZ
 * @Date 2021/5/25 10:36
 **/

import de.rub.nds.tlsattacker.core.protocol.message.HandshakeMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.record.AbstractRecord;

import java.util.List;

/**
 * Comprises the result of packing a list of messages into records.
 * 包含将消息列表打包到记录中的结果
 */
public class PackingResult {
    private List<ProtocolMessage> messages;
    private List<AbstractRecord> records;
    private List<HandshakeMessage> handshakeMessages;
    public PackingResult(List<ProtocolMessage> messages,
                         List<AbstractRecord> records) {
        super();
        this.messages = messages;
        this.records = records;
    }
    public PackingResult(List<ProtocolMessage> messages,
                         List<AbstractRecord> records,
                         List<HandshakeMessage> handshakeMessages) {
        super();
        this.messages = messages;
        this.records = records;
        this.handshakeMessages = handshakeMessages;
    }
    public List<ProtocolMessage> getMessages() {
        return messages;
    }
    public List<AbstractRecord> getRecords() {
        return records;
    }
    public List<HandshakeMessage> getHandshakeMessages() {
        return handshakeMessages;
    }
}

