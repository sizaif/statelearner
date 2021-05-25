package nl.cypherpunk.statelearner.Sut.io;

/**
 * @ClassName AlphabetPojo
 * @Auther SIZ
 * @Date 2021/5/25 14:51
 **/

import nl.cypherpunk.statelearner.Sut.io.message.*;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * POJO class used for .xml (de-)serialization.
 */
@XmlRootElement(name = "alphabet")
@XmlAccessorType(XmlAccessType.FIELD)
public class AlphabetPojo {

    @XmlElements(value = {
            @XmlElement(type = GenericTlsInput.class, name = "GenericTlsInput"),
            @XmlElement(type = ChangeCipherSpecInput.class, name = "ChangeCipherSpecInput"),
            @XmlElement(type = ClientHelloInput.class, name = "ClientHelloInput"),
            @XmlElement(type = ClientHelloWithSessionIdInput.class, name = "ClientHelloWithSessionIdInput"),
            @XmlElement(type = ClientHelloRenegotiationInput.class, name = "ClientHelloRenegotiation"),
            @XmlElement(type = FinishedInput.class, name = "FinishedInput"),
            @XmlElement(type = ClientKeyExchangeInput.class, name = "ClientKeyExchangeInput")})
    private List<TlsInput> inputs;

    public AlphabetPojo() {
    }

    public AlphabetPojo(List<TlsInput> words) {
        this.inputs = words;
    }

    public List<TlsInput> getWords() {
        return inputs;
    }
}
