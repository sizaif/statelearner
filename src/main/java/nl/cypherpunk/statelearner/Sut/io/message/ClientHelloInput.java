package nl.cypherpunk.statelearner.Sut.io.message;

import de.rub.nds.modifiablevariable.bytearray.ByteArrayExplicitValueModification;
import de.rub.nds.modifiablevariable.bytearray.ModifiableByteArray;
import de.rub.nds.tlsattacker.core.constants.CipherSuite;
import de.rub.nds.tlsattacker.core.protocol.message.ClientHelloMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.state.State;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Arrays;

/**
 * @ClassName ClientHelloInput
 * @Auther SIZ
 * @Date 2021/5/25 15:06
 **/
public class ClientHelloInput extends NamedTlsInput {

    @XmlAttribute(name = "suite", required = true)
    private CipherSuite suite;
//    /**
//     * option needed to learn DTLS implementations which use cookie-less
//     * handshake messages
//     */
//    @XmlAttribute(name = "forceDigest", required = false)
//    private boolean forceDigest = false;
//
    /**
     * option for resetting the digests
     */
    @XmlAttribute(name = "resetDigest", required = false)
    private boolean resetDigest = true;

    /**
     * option for including the latest session id in the client hello
     */
    @XmlAttribute(name = "withSessionId", required = false)
    private boolean withSessionId = false;

    private ProtocolMessage message;

    public ClientHelloInput() {
        super("CLIENT_HELLO");
    }

    public ClientHelloInput(CipherSuite cipherSuite) {
        super(cipherSuite.name() + "_CLIENT_HELLO");
        this.suite = cipherSuite;
    }
    @Override
    public ProtocolMessage generateMessage(State state) {

        state.getConfig().setDefaultClientSupportedCiphersuites(Arrays.asList(suite));
        if(suite.name().contains("EC")){
            state.getConfig().setAddECPointFormatExtension(true);
            state.getConfig().setAddEllipticCurveExtension(true);
        }else
        {
            state.getConfig().setAddECPointFormatExtension(false);
            state.getConfig().setAddEllipticCurveExtension(false);
        }

        if (resetDigest) {
            state.getTlsContext().getDigest().reset();
        }
        ClientHelloMessage message = new ClientHelloMessage(state.getConfig());

        // we exclude the sessionId
        if (!withSessionId) {
            ModifiableByteArray sbyte = new ModifiableByteArray();
            sbyte.setModification(new ByteArrayExplicitValueModification(new byte[]{}));
            message.setSessionId(sbyte);
        }

        this.message = message;
        return message;
    }


    @Override
    public TlsInputType getInputType() {
        return null;
    }
}
