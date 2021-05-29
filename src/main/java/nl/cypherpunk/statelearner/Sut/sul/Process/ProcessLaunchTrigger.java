package nl.cypherpunk.statelearner.Sut.sul.Process;

/**
 * When should a process be (re-)started. In case of a restart, the previous
 * process is terminated.
 */
public enum ProcessLaunchTrigger {
	START, NEW_TEST
}
