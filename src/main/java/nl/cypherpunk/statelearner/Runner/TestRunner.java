package nl.cypherpunk.statelearner.Runner;

import de.learnlib.api.oracle.MembershipOracle;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import nl.cypherpunk.statelearner.Config.TestRunnerConfig;
import nl.cypherpunk.statelearner.Sut.io.message.TlsInput;
import nl.cypherpunk.statelearner.Sut.io.message.TlsOutput;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName TestRunner
 * @Auther SIZ
 * @Date 2021/5/26 17:54
 **/
public class TestRunner {

    public static final Logger LOGGER = LogManager.getLogger(TestRunner.class);
    private MembershipOracle.MealyMembershipOracle<TlsInput, TlsOutput> sulOracle;
    private TestRunnerConfig config;
    private Alphabet<TlsInput> alphabet;

    public TestRunner(TestRunnerConfig config, Alphabet<TlsInput> alphabet,
                      MembershipOracle.MealyMembershipOracle<TlsInput, TlsOutput> sutOracle) {
        this.alphabet = alphabet;
        this.config = config;
        this.sulOracle = sutOracle;
    }
    public List<TestRunnerResult<TlsInput,TlsOutput>> runTests() throws IOException{
        TestParser testParser = new TestParser();

        List<Word<TlsInput>> tests = testParser.readTests(alphabet, config.getTest());

        System.out.println("runTests testsParser");
        List<TestRunnerResult<TlsInput, TlsOutput>> results = tests.stream().map(t -> runTest(t)).collect(Collectors.toList());

        return results;
    }
    private TestRunnerResult<TlsInput, TlsOutput> runTest(Word<TlsInput> test) {
        System.out.println("runtest word");
        TestRunnerResult<TlsInput, TlsOutput> result = runTest(test, config.getTimes(), sulOracle);
        return result;
    }

    public static <I, O> TestRunnerResult<I, O> runTest(Word<I> test,
                                                        int times, MembershipOracle.MealyMembershipOracle<I, O> sulOracle) {
        LinkedHashMap<Word<O>, Integer> answerMap = new LinkedHashMap<>();
        for (int i = 0; i < times; i++) {
            Word<O> answer = sulOracle.answerQuery(test);
            System.out.println(i);
            if (!answerMap.containsKey(answer)) {
                answerMap.put(answer, 1);
            } else {
                answerMap.put(answer, answerMap.get(answer) + 1);
            }
        }
        return new TestRunnerResult<I, O>(test, answerMap);
    }

}