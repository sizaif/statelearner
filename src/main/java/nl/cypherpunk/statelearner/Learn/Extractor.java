package nl.cypherpunk.statelearner.Learn;

/**
 * @ClassName Extractor
 * @Auther SIZ
 * @Date 2021/5/26 22:45
 **/

import net.automatalib.words.Alphabet;
import nl.cypherpunk.statelearner.CleanupTasks;
import nl.cypherpunk.statelearner.Config.TLSFuzzerConfig;
import nl.cypherpunk.statelearner.Sut.io.message.TlsInput;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * model learning to generate model for a given tls server implementation
 */
public class Extractor {

    // Names of some generated output files
    public static final String LEARNED_MODEL_FILENAME = "learnedModel.dot";
    public static final String STATISTICS_FILENAME = "statistics.txt";
    public static final String SUL_CONFIG_FILENAME = "sul.config";
    public static final String ALPHABET_FILENAME = "alphabet.xml";
    public static final String NON_DET_FILENAME = "nondet.log";
    private static final String ERROR_FILENAME = "error.msg";

    private static final Logger LOG = Logger.getLogger(Extractor.class.getName());
    private final TLSFuzzerConfig fuzzerConfig;
    private final Alphabet<TlsInput> alphabet;
    private final CleanupTasks cleanupTasks;

    public Extractor(TLSFuzzerConfig fconfig, Alphabet<TlsInput> alphabet, CleanupTasks tasks){
        this.fuzzerConfig =fconfig;
        this.alphabet = alphabet;
        this.cleanupTasks = tasks;
    }

    public static class  ExtractorResult {
        public StateMachine learnedModel;
        public Statistics statistics;
        public final List<StateMachine> hypotheses;
        public File learnedModelFile;
    }
}
