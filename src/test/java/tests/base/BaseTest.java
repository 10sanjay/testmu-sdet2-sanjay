package tests.base;

import core.ConfigManager;
import core.DriverManager;
import core.ScenarioContext;
import io.restassured.RestAssured;
import listeners.TestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.AllureUtils;
import utils.ScreenshotUtil;

@Listeners(TestListener.class)
public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void initApi() {
        RestAssured.baseURI = ConfigManager.get("api.baseUrl");
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("") String browser) {
        if (requiresBrowser()) {
            String b = (browser == null || browser.isBlank())
                    ? ConfigManager.get("browser.name") : browser;
            DriverManager.initDriver(b);
        }
    }

    /** 🔑 Capture screenshot BEFORE driver is quit. */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (requiresBrowser() && DriverManager.getDriver() != null) {
            // 📸 Take screenshot on failure WHILE the driver is still alive
            if (result.getStatus() == ITestResult.FAILURE) {
                AllureUtils.attachScreenshot(
                        result.getMethod().getMethodName(),
                        ScreenshotUtil.capture()
                );
            }
            DriverManager.quitDriver();
        }
        ScenarioContext.clear();
    }

    /** True if the test package needs a browser. API tests skip driver init. */
    protected boolean requiresBrowser() {
        String pkg = this.getClass().getPackage().getName();
        return !pkg.endsWith(".api");
    }
}