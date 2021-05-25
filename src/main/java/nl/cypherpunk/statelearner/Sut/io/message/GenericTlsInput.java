package nl.cypherpunk.statelearner.Sut.io.message;

/**
 * @ClassName GenericTlsInput
 * @Auther SIZ
 * @Date 2021/5/25 14:53
 **/

import de.rub.nds.modifiablevariable.ModifiableVariable;
import de.rub.nds.tlsattacker.core.https.HttpsRequestMessage;
import de.rub.nds.tlsattacker.core.https.HttpsResponseMessage;
import de.rub.nds.tlsattacker.core.protocol.ModifiableVariableHolder;
import de.rub.nds.tlsattacker.core.protocol.message.*;
import de.rub.nds.tlsattacker.core.state.State;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * A GenericTlsInput is executed by sending a supplied TLS-Attacker protocol
 * message. Note that the encapsulated message is currently re-used. This makes
 * the input unsuitable for messages whose contents can change between
 * executions (such as Finished or ClientHello). For these messages,
 * corresponding TlsInput subclasses should be defined.
 */
public class GenericTlsInput extends NamedTlsInput {
    @XmlElements(value = {
            @XmlElement(type = ProtocolMessage.class, name = "ProtocolMessage"),
            @XmlElement(type = CertificateMessage.class, name = "Certificate"),
            @XmlElement(type = CertificateVerifyMessage.class, name = "CertificateVerify"),
            @XmlElement(type = CertificateRequestMessage.class, name = "CertificateRequest"),
            @XmlElement(type = ClientHelloMessage.class, name = "ClientHello"),
            @XmlElement(type = HelloVerifyRequestMessage.class, name = "HelloVerifyRequest"),
            @XmlElement(type = DHClientKeyExchangeMessage.class, name = "DHClientKeyExchange"),
            @XmlElement(type = DHEServerKeyExchangeMessage.class, name = "DHEServerKeyExchange"),
            @XmlElement(type = ECDHClientKeyExchangeMessage.class, name = "ECDHClientKeyExchange"),
            @XmlElement(type = ECDHEServerKeyExchangeMessage.class, name = "ECDHEServerKeyExchange"),
            @XmlElement(type = PskClientKeyExchangeMessage.class, name = "PSKClientKeyExchange"),
            @XmlElement(type = FinishedMessage.class, name = "Finished"),
            @XmlElement(type = RSAClientKeyExchangeMessage.class, name = "RSAClientKeyExchange"),
            @XmlElement(type = GOSTClientKeyExchangeMessage.class, name = "GOSTClientKeyExchange"),
            @XmlElement(type = ServerHelloDoneMessage.class, name = "ServerHelloDone"),
            @XmlElement(type = ServerHelloMessage.class, name = "ServerHello"),
            @XmlElement(type = AlertMessage.class, name = "Alert"),
            @XmlElement(type = NewSessionTicketMessage.class, name = "NewSessionTicket"),
            @XmlElement(type = ApplicationMessage.class, name = "Application"),
            @XmlElement(type = ChangeCipherSpecMessage.class, name = "ChangeCipherSpec"),
            @XmlElement(type = SSL2ClientHelloMessage.class, name = "SSL2ClientHello"),
            @XmlElement(type = SSL2ServerHelloMessage.class, name = "SSL2ServerHello"),
            @XmlElement(type = SSL2ClientMasterKeyMessage.class, name = "SSL2ClientMasterKey"),
            @XmlElement(type = SSL2ServerVerifyMessage.class, name = "SSL2ServerVerify"),
            @XmlElement(type = UnknownMessage.class, name = "UnknownMessage"),
            @XmlElement(type = UnknownHandshakeMessage.class, name = "UnknownHandshakeMessage"),
            @XmlElement(type = HelloRequestMessage.class, name = "HelloRequest"),
            @XmlElement(type = HeartbeatMessage.class, name = "Heartbeat"),
            @XmlElement(type = SupplementalDataMessage.class, name = "SupplementalDataMessage"),
            @XmlElement(type = EncryptedExtensionsMessage.class, name = "EncryptedExtensionMessage"),
            @XmlElement(type = HttpsRequestMessage.class, name = "HttpsRequest"),
            @XmlElement(type = HttpsResponseMessage.class, name = "HttpsResponse"),
            @XmlElement(type = PskClientKeyExchangeMessage.class, name = "PskClientKeyExchange"),
            @XmlElement(type = PskDhClientKeyExchangeMessage.class, name = "PskDhClientKeyExchange"),
            @XmlElement(type = PskDheServerKeyExchangeMessage.class, name = "PskDheServerKeyExchange"),
            @XmlElement(type = PskEcDhClientKeyExchangeMessage.class, name = "PskEcDhClientKeyExchange"),
            @XmlElement(type = PskEcDheServerKeyExchangeMessage.class, name = "PskEcDheServerKeyExchange"),
            @XmlElement(type = PskRsaClientKeyExchangeMessage.class, name = "PskRsaClientKeyExchange"),
            @XmlElement(type = PskServerKeyExchangeMessage.class, name = "PskServerKeyExchange"),
            @XmlElement(type = SrpServerKeyExchangeMessage.class, name = "SrpServerKeyExchange"),
            @XmlElement(type = SrpClientKeyExchangeMessage.class, name = "SrpClientKeyExchange"),
            @XmlElement(type = EndOfEarlyDataMessage.class, name = "EndOfEarlyData"),
            @XmlElement(type = EncryptedExtensionsMessage.class, name = "EncryptedExtensions"),
            @XmlElement(type = HelloRetryRequestMessage.class, name = "HelloRetryRequest")})
    private ProtocolMessage message;

    public GenericTlsInput() {
        super(null);
    }

    public GenericTlsInput(ProtocolMessage message) {
        super(message.toCompactString());
        this.message = message;
    }

    public ProtocolMessage generateMessage(State state) {
        stripFields(message);
        // message.getHandler(state.getTlsContext()).prepareMessage(message,
        // prepare);
        return message;
    }

    // this is only useful for deserializing once learning is done
    public void postReceiveUpdate(TlsOutput output, State state) {
        stripFields(message);
    }

    // if the name is not set, we use the compact string of a message as a name
    public String toString() {
        String name = getName();
        if (name == null)
            return message.toCompactString();
        else
            return name;
    }

    /*
     * Sets the original value of all modifiable variable fields to null.
     */
    private void stripFields(ProtocolMessage message) {
        List<ModifiableVariableHolder> holders = new LinkedList<>();
        holders.addAll(message.getAllModifiableVariableHolders());
        for (ModifiableVariableHolder holder : holders) {
            List<Field> fields = holder.getAllModifiableVariableFields();
            for (Field f : fields) {
                f.setAccessible(true);

                ModifiableVariable mv = null;
                try {
                    mv = (ModifiableVariable) f.get(holder);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                if (mv != null) {
                    if (mv.getModification() != null
                            || mv.isCreateRandomModification()) {
                        mv.setOriginalValue(null);
                    } else {
                        try {
                            f.set(holder, null);
                        } catch (IllegalArgumentException
                                | IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public TlsInputType getInputType() {
        return TlsInputType.fromProtocolMessageType(message
                .getProtocolMessageType());
    }
}
