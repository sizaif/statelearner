package nl.cypherpunk.statelearner.execute;

import java.util.List;
import java.util.stream.Collectors;

import de.rub.nds.tlsattacker.core.protocol.message.HandshakeMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.record.AbstractRecord;

/**
 * @ClassName ExecutionContext
 * @Auther SIZ
 * @Date 2021/5/25 10:30
 **/
public class ExecutionContext {

    private List<StepContext> stepContextList;

    // get a setpcontext form setpcontextlist
    public StepContext getStepContext(){
        if (!stepContextList.isEmpty())
            return stepContextList.get(stepContextList.size() - 1);
        return null;
    }
    public List<StepContext> getStepContextList() {
        return stepContextList;
    }
//    public List<HandshakeMessage> getFragmentsSent(){
//        return stepContextList
//                .stream()
//                .filter(s -> s.getFragmentationResult() != null)
//                .flatMap(
//                        fs -> fs.getFragmentationResult().getFragments()
//                                .stream().collect(Collectors.toList()));
//    }
    public List<HandshakeMessage> getHandshakeMessage(){
        return stepContextList.stream().filter(s -> s.getPackingResult() != null)
                .flatMap(s -> s.getPackingResult().getHandshakeMessages().stream())
                .collect(Collectors.toList());
    }
    public List<ProtocolMessage> getMessagesSent(){

        return stepContextList.stream().filter(s -> s.getPackingResult() != null)
                .flatMap(s -> s.getPackingResult().getMessages().stream())
                .collect(Collectors.toList());
    }
    public List<AbstractRecord> getRecordsSent(){
        return stepContextList.stream().filter(s -> s.getPackingResult() != null)
                .flatMap(s -> s.getPackingResult().getRecords().stream())
                .collect(Collectors.toList());
    }
    // get index SetContext
    public StepContext getSetContext(int index){
        return stepContextList.get(index);
    }
    public int getStepCount(){
        return stepContextList.size();
    }
}
