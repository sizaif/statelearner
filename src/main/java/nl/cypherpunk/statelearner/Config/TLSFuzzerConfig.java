package nl.cypherpunk.statelearner.Config;

/**
 * @ClassName TLSFuzzerConfig
 * @Auther SIZ
 * @Date 2021/5/18 13:06
 **/

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import nl.cypherpunk.statelearner.Learn.EquivalenceAlgorithmName;
import nl.cypherpunk.statelearner.Learn.LearningAlgorithmName;
import de.rub.nds.tlsattacker.core.config.delegate.GeneralDelegate;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class TLSFuzzerConfig {

    @Parameter(names = "-specification", required = false, description = "A model of the specification. For examples, look at './examples/specifications/'. "
            + "If a debug test is executed (via -test), the test will be run both on the system and on the model.")
    private String specification = null;

    @Parameter(names = "-alphabet", required = false, description = "An .xml file defining the input alphabet. "
            + "The alphabet is used to interpret inputs from a given specification, as well as to learn. "
            + "Each input in the alphabet has a name under which it appears in the specification."
            + "The name is defaulted mostly to the capitalized snake case of the message involved."
            + "For example, for <FinishedInput/> the name is FINISHED."
            + "The name can be changed by setting the 'name' attribute")
    private String alphabet = null;

    @Parameter(names = "-output", required = false, description = "The folder in which results should be saved")
    private String output = "output";

    @ParametersDelegate
    private SulDelegate sulDelegate;

    @ParametersDelegate
    private TLSLearningConfig learningConfig;

    @ParametersDelegate
    private TestRunnerConfig testRunnerConfig;

    @ParametersDelegate
    private GeneralDelegate generalDelegate;

    public TLSFuzzerConfig() {
        generalDelegate = new GeneralDelegate();
        sulDelegate = new SulDelegate();
        learningConfig = new TLSLearningConfig();
        testRunnerConfig = new TestRunnerConfig();
    }

    public GeneralDelegate getGeneralDelegate() {
        return generalDelegate;
    }

    public SulDelegate getSulDelegate() {
        return sulDelegate;
    }

    public TLSLearningConfig getLearningConfig() {
        return learningConfig;
    }

    public TestRunnerConfig getTestRunnerConfig() {
        return testRunnerConfig;
    }

    public String getSpecification() {
        return specification;
    }

    public String getOutput() {
        return output;
    }

    public String getAlphabet() {
        return alphabet;
    }

    @Override
    public String toString() {
        return "TLSFuzzerConfig{" +
                "specification='" + specification + '\'' +
                ", alphabet='" + alphabet + '\'' +
                ", output='" + output + '\'' +
                ", sulDelegate=" + sulDelegate +
                ", learningConfig=" + learningConfig +
                ", testRunnerConfig=" + testRunnerConfig +
                ", generalDelegate=" + generalDelegate +
                '}';
    }
}
