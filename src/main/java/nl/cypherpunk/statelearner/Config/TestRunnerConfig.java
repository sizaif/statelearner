package nl.cypherpunk.statelearner.Config;

import com.beust.jcommander.Parameter;

/**
 * @ClassName TestRunnerConfig
 * @Auther SIZ
 * @Date 2021/5/18 13:17
 **/
public class TestRunnerConfig {

    @Parameter(names = "-test", required = false, description = "Debug option, instead of learning, executes a test in the given file and exits. For example of test files, see './examples/tests/'.")
    private String test = null;

    @Parameter(names = "-times", required = false, description = "The number of times the tests should be run")
    private Integer times = 1;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
