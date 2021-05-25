package nl.cypherpunk.statelearner.Sut.io;

import net.automatalib.words.Alphabet;
import nl.cypherpunk.statelearner.Config.TLSFuzzerConfig;
import nl.cypherpunk.statelearner.Sut.io.message.TlsInput;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName AlphabetFactory
 * @Auther SIZ
 * @Date 2021/5/25 14:44
 **/
public class AlphabetFactory {

    public static final Logger LOGGER = LogManager.getLogger(AlphabetFactory.class);

    public static final String DEFAULT_ALPHABET = "/default_alphabet.xml";

    public static Alphabet<TlsInput> buildAlphabet(TLSFuzzerConfig config){
        Alphabet<TlsInput> alphabet = null;
        if(config.getAlphabet() != null){
            try {
                alphabet = AlphabetFactory.buildConfiguredAlphabet(config);
            }catch (JAXBException | IOException | XMLStreamException e) {
                LOGGER.fatal("Failed to instantiate alphabet");
                LOGGER.fatal(e);
                System.exit(0);
            }
        }else{
            try {
                alphabet = AlphabetFactory.buildDefaultAlphabet();
            }catch (JAXBException | IOException | XMLStreamException e) {
                LOGGER.fatal("Failed to instantiate default alphabet");
                LOGGER.fatal(e);
                System.exit(0);
            }
        }
        return alphabet;
    }
    public static Alphabet<TlsInput> buildDefaultAlphabet() throws JAXBException, IOException, XMLStreamException {
        return AlphabetSerializer.read(AlphabetFactory.class.getResourceAsStream(DEFAULT_ALPHABET));
    }
    public static Alphabet<TlsInput> buildConfiguredAlphabet(TLSFuzzerConfig config) throws FileNotFoundException,
            JAXBException, IOException, XMLStreamException {
        Alphabet<TlsInput> alphabet = null;
        if (config.getAlphabet() != null) {
            alphabet = AlphabetSerializer.read(new FileInputStream(config.getAlphabet()));
        }
        return alphabet;
    }
}
