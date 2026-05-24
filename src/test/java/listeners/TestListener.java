package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer, IConfigurationListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override public void onTestStart(ITestResult r)   { log.info("▶ START: {}", r.getMethod().getMethodName()); }
    @Override public void onTestSuccess(ITestResult r) { log.info("✔ PASS:  {}", r.getMethod().getMethodName()); }

    @Override
    public void onTestFailure(ITestResult r) {
        log.error("✘ FAIL:  {}", r.getMethod().getMethodName());
        if (r.getThrowable() != null) {
            log.error("   Reason: {}", r.getThrowable().getMessage());
        }
        // Screenshot is now captured in BaseTest.tearDown() — driver-safe
    }

    @Override
    public void onTestSkipped(ITestResult r) {
        log.warn("⊘ SKIP:  {}", r.getMethod().getMethodName());
        if (r.getThrowable() != null) log.error("   Reason: {}", r.getThrowable().toString());
    }

    @Override
    public void onConfigurationFailure(ITestResult r) {
        log.error("⚠ CONFIG FAILURE in: {}", r.getMethod().getMethodName());
        if (r.getThrowable() != null) r.getThrowable().printStackTrace();
    }

    @Override
    public void transform(ITestAnnotation a, Class c, Constructor ctor, Method m) {
        a.setRetryAnalyzer(RetryAnalyzer.class);
    }
}