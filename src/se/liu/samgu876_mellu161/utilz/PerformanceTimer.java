package se.liu.samgu876_mellu161.utilz;

import static se.liu.samgu876_mellu161.utilz.HelpFunctions.nanoToMs;

/**
 * Utility class for measuring and printing the execution time of code blocks.
 * Useful for debugging.
 */
public class PerformanceTimer
{
    private long startTime;
    private String label;

    public PerformanceTimer(final String label) {
	this.label = label;
    }

    public void start() {
	startTime = System.nanoTime();
    }

    public void stopAndPrint() {
	long endTime = System.nanoTime();
	double durationMs = nanoToMs(endTime - startTime);
	System.out.printf("[%s]: %.3f ms%n", label, durationMs);
    }
}
