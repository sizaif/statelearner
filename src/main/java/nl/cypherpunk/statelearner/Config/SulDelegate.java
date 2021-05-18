package nl.cypherpunk.statelearner.Config;

import com.beust.jcommander.Parameter;
import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.config.delegate.ClientDelegate;
import de.rub.nds.tlsattacker.core.constants.ProtocolVersion;
import de.rub.nds.tlsattacker.core.exceptions.ConfigurationException;
import nl.cypherpunk.statelearner.Sut.ProcessLaunchTrigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SulDelegate
 * @Auther SIZ
 * @Date 2021/5/18 13:17
 **/
public class SulDelegate extends ClientDelegate {
    public static final String SUL_CONFIG = "/sul.config";
    /**
     * Template variable for fuzzer root directory which can be used in path
     * specification (e.g. when supplying the alphabet) and is solved at
     * runtime. This avoids the use of hard-coded absolute paths which tie SUL
     * arguments to the local environment in which the SUL is run.
     */
    public static final String FUZZER_DIR = "fuzzer.dir";

    @Parameter(names = "-protocol", required = false, description = "Protocol analyzed, determines transport layer used", converter = ProtocolVersionConverter.class)
    private ProtocolVersion protocolVersion = ProtocolVersion.TLS12;

    @Parameter(names = "-timeout", required = false, description = "Time the SUL spends waiting for a response (response timeout)")
    private Integer timeout = 100;

    @Parameter(names = "-rstWait", required = false, description = "Time the SUL waits after executing each query (reset timeout)")
    private Long resetWait = 0L;

    @Parameter(names = {"-command", "-cmd"}, required = false, description = "Command for starting the (D)TLS process")
    private String command = null;

    @Parameter(names = {"-terminateCommand", "-termCmd"}, required = false, description = "Command for terminating the (D)TLS process. If specified, it is used instead of java.lang.Process#destroy()")
    private String terminateCommand = null;

    @Parameter(names = {"-processDir"}, required = false, description = "The directory of the (D)TLS process")
    private String processDir = null;

    @Parameter(names = {"-processTrigger"}, required = false, description = "When is the process launched")
    private ProcessLaunchTrigger processTrigger = ProcessLaunchTrigger.NEW_TEST;

    @Parameter(names = "-runWait", required = false, description = "Time the SUL waits after running each TLS command (start timeout)")
    private Long runWait = 0L;

    @Parameter(names = "-resetPort", required = false, description = "Port to which to send a reset command")
    private Integer resetPort = null;

    @Parameter(names = "-resetAddress", required = false, description = "Address to which to send a reset command")
    private String resetAddress = "localhost";

    @Parameter(names = "-resetCommandWait", required = false, description = "Time waited after sending a reset command")
    private Long resetCommandWait = 0L;

    @Parameter(names = "-resetAck", required = false, description = "Wait from acknowledgement from the other side")
    private boolean resetAck = false;

    @Parameter(names = "-sulConfig", required = false, description = "Configuration for the SUL")
    private String sulConfig = null;

    @Parameter(names = "-withApplicationOutput", required = false, description = "Includes the application output in the output generated. Only useful the command was provided")
    private boolean withApplicationOutput = false;

    @Parameter(names = "-repeatingOutputs", required = false, description = "Specifies the outputs that the SUL is expected to repeat an arbitrary number of times, in response to an input. ")
    private List<String> repeatingOutputs = null;

    // so we don't replaceAll each time
    private Map<String, String> resolutionCache = new HashMap<>();

    public SulDelegate() {
        super();
    }

    /*
     * Replaces instances of ${FUZZER_DIR} in strings
     */
    private String resolveHomeTemplate(String str) {
        if (str == null || !str.contains(FUZZER_DIR))
            return str;

        if (resolutionCache.containsKey(str))
            return resolutionCache.get(str);

        String homeVal = System.getProperty(FUZZER_DIR);
        if (homeVal == null)
            homeVal = System.getProperty("user.dir");

        String resolvedStr = str.replaceAll("\\$\\{" + FUZZER_DIR + "\\}",
                homeVal);
        resolutionCache.put(str, resolvedStr);
        return resolvedStr;
    }

    public void applyDelegate(Config config) throws ConfigurationException {
        super.applyDelegate(config);
        config.getDefaultClientConnection().setTimeout(timeout);
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Long getResetWait() {
        return resetWait;
    }

    public void setResetWait(Long resetWait) {
        this.resetWait = resetWait;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTerminateCommand() {
        return terminateCommand;
    }

    public void setTerminateCommand(String terminateCommand) {
        this.terminateCommand = terminateCommand;
    }

    public String getProcessDir() {
        return processDir;
    }

    public void setProcessDir(String processDir) {
        this.processDir = processDir;
    }

    public ProcessLaunchTrigger getProcessTrigger() {
        return processTrigger;
    }

    public void setProcessTrigger(ProcessLaunchTrigger processTrigger) {
        this.processTrigger = processTrigger;
    }

    public Long getRunWait() {
        return runWait;
    }

    public void setRunWait(Long runWait) {
        this.runWait = runWait;
    }

    public Integer getResetPort() {
        return resetPort;
    }

    public void setResetPort(Integer resetPort) {
        this.resetPort = resetPort;
    }

    public String getResetAddress() {
        return resetAddress;
    }

    public void setResetAddress(String resetAddress) {
        this.resetAddress = resetAddress;
    }

    public Long getResetCommandWait() {
        return resetCommandWait;
    }

    public void setResetCommandWait(Long resetCommandWait) {
        this.resetCommandWait = resetCommandWait;
    }

    public boolean isResetAck() {
        return resetAck;
    }

    public void setResetAck(boolean resetAck) {
        this.resetAck = resetAck;
    }

    public String getSulConfig() {
        return sulConfig;
    }

    public void setSulConfig(String sulConfig) {
        this.sulConfig = sulConfig;
    }

    public boolean isWithApplicationOutput() {
        return withApplicationOutput;
    }

    public void setWithApplicationOutput(boolean withApplicationOutput) {
        this.withApplicationOutput = withApplicationOutput;
    }

    public List<String> getRepeatingOutputs() {
        return repeatingOutputs;
    }

    public void setRepeatingOutputs(List<String> repeatingOutputs) {
        this.repeatingOutputs = repeatingOutputs;
    }

    public Map<String, String> getResolutionCache() {
        return resolutionCache;
    }

    public void setResolutionCache(Map<String, String> resolutionCache) {
        this.resolutionCache = resolutionCache;
    }
}