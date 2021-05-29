package nl.cypherpunk.statelearner.Sut.io.definitions;



import nl.cypherpunk.statelearner.Sut.io.message.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
public class InputDefinition {
	private String name;
	@XmlElements(value = {
			@XmlElement(type = TlsInput.class, name = "TlsInput"),
			@XmlElement(type = ClientHelloInput.class, name = "ClientHelloInput"),
			@XmlElement(type = ClientHelloWithSessionIdInput.class, name = "ClientHelloWithSessionIdInput"),
			@XmlElement(type = ClientHelloRenegotiationInput.class, name = "ClientHelloRenegotiation"),
			@XmlElement(type = GenericTlsInput.class, name = "GenericTlsInput"),
			@XmlElement(type = FinishedInput.class, name = "FinishedInput"),
			@XmlElement(type = ChangeCipherSpecInput.class, name = "ChangeCipherSpecInput"),})
	private TlsInput input;

	public InputDefinition() {
	}

	public InputDefinition(String name, TlsInput input) {
		super();
		this.name = name;
		this.input = input;
	}

	public String getName() {
		return name;
	}
	public TlsInput getInput() {
		return input;
	}

}
