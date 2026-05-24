package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class RetryUtil {

    private static final Logger log = LoggerFactory.getLogger(RetryUtil.class);

    private RetryUtil() {}

    public static <T> T withRetry(Supplier<T> action, int maxAttempts, long delayMs) {
        RuntimeException last = null;
        for (int i = 1; i <= maxAttempts; i++) {
            try {
                return action.get();
            } catch (RuntimeException e) {        
                last = e;
                log.warn("Attempt {}/{} failed: {}", i, maxAttempts, e.getMessage());
                sleep(delayMs);
            }
        }
        throw last;
    }

    public static void withRetry(Runnable action, int maxAttempts, long delayMs) {
        withRetry(() -> { action.run(); return null; }, maxAttempts, delayMs);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}