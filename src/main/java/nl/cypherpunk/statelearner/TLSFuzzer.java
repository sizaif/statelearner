package nl.cypherpunk.statelearner;



import de.learnlib.api.SUL;
import de.learnlib.api.oracle.MembershipOracle.MealyMembershipOracle;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.automata.transducers.impl.FastMealy;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import nl.cypherpunk.statelearner.Config.TLSFuzzerConfig;
import nl.cypherpunk.statelearner.Execute.TestingInputExecutor;
import nl.cypherpunk.statelearner.Runner.TestRunner;
import nl.cypherpunk.statelearner.Runner.TestRunnerResult;
import nl.cypherpunk.statelearner.Sut.io.AlphabetFactory;
import nl.cypherpunk.statelearner.Sut.io.definitions.Definitions;
import nl.cypherpunk.statelearner.Sut.io.definitions.DefinitionsFactory;
import nl.cypherpunk.statelearner.Sut.io.message.TlsInput;
import nl.cypherpunk.statelearner.Sut.io.message.TlsOutput;
import nl.cypherpunk.statelearner.Sut.sul.Process.IsAliveWrapper;
import nl.cypherpunk.statelearner.Sut.sul.Process.TlsProcessWrapper;
import nl.cypherpunk.statelearner.Sut.sul.ResettingWrapper;
import nl.cypherpunk.statelearner.Sut.sul.TlsSUL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName TLSFuzzer
 * @Auther SIZ
 * @Date 2021/5/18 17:01
 **/
public class TLSFuzzer {
    private static final Logger LOGGER = LogManager.getLogger(TLSFuzzer.class);

    /**
     * fuzzer configuration which defines the mode of operation (learn model/run
     * tests), the SUL, learning/testing parameters
     */
    private TLSFuzzerConfig config;

    /**
     * collection of cleanup tasks which should by executed once learning
     * terminates
     */
    private CleanupTasks cleanupTasks;

    public TLSFuzzer(TLSFuzzerConfig config){
        this.config = config;
        this.cleanupTasks = new CleanupTasks();
    }

    /**
     * Default operation is to perform model learning of the implementation.
     * Alternatively, runs one or more tests on the implementation, summarizing
     * generated responses and optionally adjoining them by those generated by a
     * specification.
     */
    public void startTesting() throws IOException {
        try {
            System.out.println(config.getTestRunnerConfig().getTest());
            if (config.getTestRunnerConfig().getTest() != null) {
                System.out.println("runtest tt");
                runTest(config);
            } else {
                // setting up our output directory
                File folder = new File(config.getOutput());
                folder.mkdirs();
                // output to Model
                //extractModel(config);
            }
        } catch (Exception e) {
            cleanupTasks.execute();
            throw e;
        }
        cleanupTasks.execute();
    }

    /**
     * Runs test (tests) provided in the test file.
     *
     * @param config
     * @throws IOException
     */
    private void runTest(TLSFuzzerConfig config) throws IOException {
        // 创建状态机
        MealyMembershipOracle<TlsInput, TlsOutput> sutOracle = createTestOracle(config);
        System.out.println("Mealy done");
        // 创建 alphabet 映射
        Alphabet<TlsInput> alphabet = AlphabetFactory.buildAlphabet(config);
        System.out.println("Alphabet done");
        // 创建 TestRunner
        TestRunner runner = new TestRunner(config.getTestRunnerConfig(), alphabet, sutOracle);

        List<TestRunnerResult<TlsInput, TlsOutput>> results = runner.runTests();
        System.out.println(results.toString());
        for (TestRunnerResult<TlsInput, TlsOutput> result : results) {
            LOGGER.error(result.toString());
            if (config.getSpecification() != null) {
                Definitions definitions = DefinitionsFactory.generateDefinitions(alphabet);


               // MealyDotParser<TlsInput, TlsOutput> dotParser = new MealyDotParser<>(new TlsProcessor(definitions));

               // FastMealy<TlsInput, TlsOutput> machine = dotParser.parseAutomaton(config.getSpecification()).get(0);
              //  Word<TlsOutput> outputWord = machine.computeOutput(result.getInputWord());
              //  LOGGER.error("Expected output: " + outputWord);
            }
        }
    }

    /*
     * creates basic membership oracle for running tests
     */
    private MealyMembershipOracle<TlsInput, TlsOutput> createTestOracle(TLSFuzzerConfig config) {
        TlsSUL actualSut = new TlsSUL(config.getSulDelegate(), new TestingInputExecutor());
        SUL<TlsInput, TlsOutput> tlsSut = actualSut;
        if (config.getSulDelegate().getCommand() != null) {
            tlsSut = new TlsProcessWrapper(tlsSut, config.getSulDelegate());
        }
        if (config.getSulDelegate().getResetPort() != null) {
            ResettingWrapper<TlsInput, TlsOutput> wrapper = new ResettingWrapper<>(
                    tlsSut, config.getSulDelegate(), cleanupTasks);
            actualSut.setDynamicPortProvider(wrapper);
            tlsSut = wrapper;
        }
        tlsSut = new IsAliveWrapper(tlsSut);

        MealyMembershipOracle<TlsInput, TlsOutput> tlsOracle = new SULOracle<TlsInput, TlsOutput>(tlsSut);

        return tlsOracle;
    }
    /*
     * generates model + statistics for the given SUT/learning arguments
     * (alphabet, learning/test algorithm, etc.)
     */
//    private ExtractorResult extractModel(TLSFuzzerConfig config) {
//        Alphabet<TlsInput> alphabet = AlphabetFactory.buildAlphabet(config);
//        Extractor extractor = new Extractor(config, alphabet, cleanupTasks);
//        ExtractorResult result = extractor.extractStateMachine();
//        return result;
//    }
}
