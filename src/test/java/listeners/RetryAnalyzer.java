package listeners;

import core.ConfigManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private final int max;
    public RetryAnalyzer() {
        int m;
        try { m = Integer.parseInt(ConfigManager.get("retry.count")); } catch (Exception e) { m = 1; }
        this.max = m;
    }
    @Override public boolean retry(ITestResult result) { return count++ < max; }
}