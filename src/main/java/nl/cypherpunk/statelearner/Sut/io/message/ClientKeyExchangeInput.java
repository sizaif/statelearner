package nl.cypherpunk.statelearner.Sut.io.message;

/**
 * @ClassName ClientKeyExchangeInput
 * @Auther SIZ
 * @Date 2021/5/25 17:17
 **/

import de.rub.nds.tlsattacker.core.protocol.message.*;
import de.rub.nds.tlsattacker.core.state.State;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 在生成密钥之前重置上下文的预主机的输入
 * *交换信息
 */
public class ClientKeyExchangeInput extends NamedTlsInput{
    @XmlAttribute(name = "algorithm", required = true)
    private KeyExchangeAlgorithm algorithm;

    public ClientKeyExchangeInput() {
        super("CLIENT_KEY_EXCHANGE");
    }

    public ClientKeyExchangeInput(KeyExchangeAlgorithm algorithm) {
        super(algorithm + "CLIENT_KEY_EXCHANGE");
        this.algorithm = algorithm;
    }
    @Override
    public ProtocolMessage generateMessage(State state) {
        state.getTlsContext().setPreMasterSecret(null);
        if (this.algorithm == null) {
            throw new RuntimeException("Algorithm not set");
        }
        switch (this.algorithm) {
            case RSA :
                return new RSAClientKeyExchangeMessage(state.getConfig());
            case PSK :
                return new PskClientKeyExchangeMessage(state.getConfig());
            case DH :
                return new DHClientKeyExchangeMessage(state.getConfig());
            case ECDH :
                return new ECDHClientKeyExchangeMessage(state.getConfig());
            case PSK_RSA :
                return new PskRsaClientKeyExchangeMessage(state.getConfig());
            case GOST :
                return new GOSTClientKeyExchangeMessage(state.getConfig());
            case SRP :
                return new SrpClientKeyExchangeMessage(state.getConfig());
            default :
                throw new RuntimeException("Algorithm " + algorithm
                        + " not supported");

        }
    }
    @Override
    public TlsInputType getInputType() {
        return TlsInputType.HANDSHAKE;
    }
}
