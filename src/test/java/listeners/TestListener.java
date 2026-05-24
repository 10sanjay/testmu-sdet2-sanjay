package listeners;

import core.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import utils.AllureUtils;
import utils.ScreenshotUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer, IConfigurationListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override public void onTestStart(ITestResult r)   { log.info("▶ START: {}", r.getMethod().getMethodName()); }
    @Override public void onTestSuccess(ITestResult r) { log.info("✔ PASS:  {}", r.getMethod().getMethodName()); }
    @Override public void onTestSkipped(ITestResult r) {
        log.warn("⊘ SKIP:  {}", r.getMethod().getMethodName());
        if (r.getThrowable() != null) {
            log.error("   Reason: {}", r.getThrowable().toString());
            r.getThrowable().printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult r) {
        log.error("✘ FAIL:  {} -> {}", r.getMethod().getMethodName(), r.getThrowable());
        if (r.getThrowable() != null) r.getThrowable().printStackTrace();
        if (DriverManager.getDriver() != null) {
            AllureUtils.attachScreenshot(r.getMethod().getMethodName(), ScreenshotUtil.capture());
        }
    }

    // 🔥 THIS is the missing piece — prints hidden @Before* failures
    @Override
    public void onConfigurationFailure(ITestResult r) {
        log.error("⚠ CONFIG FAILURE in: {}", r.getMethod().getMethodName());
        if (r.getThrowable() != null) {
            log.error("   Cause: {}", r.getThrowable().toString());
            r.getThrowable().printStackTrace();
        }
    }

    @Override
    public void transform(ITestAnnotation a, Class c, Constructor ctor, Method m) {
        a.setRetryAnalyzer(RetryAnalyzer.class);
    }
}