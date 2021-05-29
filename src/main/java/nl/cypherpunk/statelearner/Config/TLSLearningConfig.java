package nl.cypherpunk.statelearner.Config;

import com.beust.jcommander.Parameter;
import nl.cypherpunk.statelearner.Learn.EquivalenceAlgorithmName;
import nl.cypherpunk.statelearner.Learn.LearningAlgorithmName;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName TLSLearningConfig
 * @Auther SIZ
 * @Date 2021/5/18 13:13
 **/
public class TLSLearningConfig {

    @Parameter(names = "-learningAlgorithm", description = "Which algorithm shold be used for learning")
    private LearningAlgorithmName learningAlgorithm = LearningAlgorithmName.LSTAR;

    @Parameter(names = "-equivalenceAlgorithms", description = "Which test algorithms should be used for equivalance testing")
    private List<EquivalenceAlgorithmName> equivalenceAlgorithms = Arrays
            .asList(EquivalenceAlgorithmName.MODIFIED_W_METHOD);

    @Parameter(names = "-depth", description = "Maximal depth ( W/WP Method)")
    private int maxDepth = 1;

    @Parameter(names = "-minLength", description = "Min length (random words, Random WP Method)")
    private int minLength = 5;

    @Parameter(names = "-maxLength", description = "Max length (random words)")
    private int maxLength = 15;

    @Parameter(names = "-randLength", description = "Size of the random part (Random WP Method)")
    private int randLength = 5;

    @Parameter(names = "-queries", description = "Number of queries (all)")
    private int numberOfQueries = 1000;

    @Parameter(names = "-memQueryRuns", description = "The number of times each membership query is executed before an answer is returned. Setting it to more than 1 enables an multiple-run oracle which may prevent non-determinism.")
    private int runsPerMembershipQuery = 1;

    @Parameter(names = "-memQueryRetries", description = "The number of times a membership query is executed in case cache inconsistency is detected.")
    private int membershipQueryRetries = 3;

    @Parameter(names = "-queryFile", description = "If set, logs all membership queries to this file.")
    private String queryFile;

    @Parameter(names = "-probReset", description = "Probability of stopping execution of a test after each input")
    private int probReset = 0;

    @Parameter(names = "-testFile", description = "A file with tests to be run.")
    private String testFile;

    @Parameter(names = "-seed", description = "Seed used for random value generation.")
    private long seed = 0;

    @Parameter(names = "-cacheTests", description = "Cache tests, which increases the memory footprint but improves performance. It also renders useless most forms of non-determinism sanitization")
    private boolean cacheTests = false;

    @Parameter(names = "-dontCacheTests", description = "Deprecated parameter with no effect, kept for backwards compatibility. Use -cacheTests.")
    private boolean dontCacheTests = false;

    @Parameter(names = "-ceSanitization", description = "Activates CE sanitization, which involves re-running potential CE's ensuring they are not spurious")
    private boolean ceSanitization = true;

    @Parameter(names = "-skipNonDetTests", description = "Rather than throw an exception, logs and skips tests whose execution turned out non-deterministic")
    private boolean skipNonDetTests = false;

    @Parameter(names = "-ceReruns", description = "Represents the number of times a CE is re-run in order for it to be confirmed")
    private int ceReruns = 3;

    @Parameter(names = "-probabilisticSanitization", description = "Enables probabilistic sanitization of the CEs resulting in non determinism")
    private boolean probabilisticSanitization = true;

    @Parameter(names = "-timeLimit", description = "If set, imposes a time limit on the learning experiment. Once this time ellapses, "
            + "learning is stopped and statistics for the incomplete learning run are published", converter = DurationConverter.class)
    private Duration timeLimit = null;

    public LearningAlgorithmName getLearningAlgorithm() {
        return learningAlgorithm;
    }

    public List<EquivalenceAlgorithmName> getEquivalenceAlgorithms() {
        return equivalenceAlgorithms;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getNumberOfQueries() {
        return numberOfQueries;
    }

    public int getProbReset() {
        return probReset;
    }

    public int getRandLength() {
        return randLength;
    }

    public String getTestFile() {
        return testFile;
    }

    public long getSeed() {
        return seed;
    }

    public boolean isCacheTests() {
        return cacheTests;
    }

    public int getCeReruns() {
        return ceReruns;
    }

    public String getQueryFile() {
        return queryFile;
    }

    public boolean isCeSanitization() {
        return ceSanitization;
    }

    public boolean isSkipNonDetTests() {
        return skipNonDetTests;
    }

    public boolean isProbabilisticSanitization() {
        return probabilisticSanitization;
    }

    public Duration getTimeLimit() {
        return timeLimit;
    }

    public int getRunsPerMembershipQuery() {
        return runsPerMembershipQuery;
    }

    public int getMembershipQueryRetries() {
        return membershipQueryRetries;
    }

    @Override
    public String toString() {
        return "TLSLearningConfig{" +
                "learningAlgorithm=" + learningAlgorithm +
                ", equivalenceAlgorithms=" + equivalenceAlgorithms +
                ", maxDepth=" + maxDepth +
                ", minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", randLength=" + randLength +
                ", numberOfQueries=" + numberOfQueries +
                ", runsPerMembershipQuery=" + runsPerMembershipQuery +
                ", membershipQueryRetries=" + membershipQueryRetries +
                ", queryFile='" + queryFile + '\'' +
                ", probReset=" + probReset +
                ", testFile='" + testFile + '\'' +
                ", seed=" + seed +
                ", cacheTests=" + cacheTests +
                ", dontCacheTests=" + dontCacheTests +
                ", ceSanitization=" + ceSanitization +
                ", skipNonDetTests=" + skipNonDetTests +
                ", ceReruns=" + ceReruns +
                ", probabilisticSanitization=" + probabilisticSanitization +
                ", timeLimit=" + timeLimit +
                '}';
    }
}
