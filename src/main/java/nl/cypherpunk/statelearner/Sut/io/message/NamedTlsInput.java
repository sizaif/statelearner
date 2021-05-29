package nl.cypherpunk.statelearner.Sut.io.message;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @ClassName NamedTlsInput
 * @Auther SIZ
 * @Date 2021/5/25 14:54
 **/
public abstract class NamedTlsInput extends TlsInput {

    /**
     * The name by which the input can be referred
     */
    @XmlAttribute(name = "name", required = true)
    private String name = null;
    protected NamedTlsInput() {
        super();
    }
    protected NamedTlsInput(String name) {
        super();
        this.name = name;
    }

    public String toString() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }
}
