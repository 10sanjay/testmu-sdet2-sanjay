package tests.base;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import core.ConfigManager;
import core.DriverManager;
import core.ScenarioContext;
import io.restassured.RestAssured;
import listeners.TestListener;
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
                    ? ConfigManager.get("browser.name")
                    : browser;
            DriverManager.initDriver(b);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (requiresBrowser() && DriverManager.getDriver() != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                AllureUtils.attachScreenshot(
                        result.getMethod().getMethodName(),
                        ScreenshotUtil.capture());
            }
            try {

                DriverManager.getDriver().manage().deleteAllCookies();
                ((org.openqa.selenium.JavascriptExecutor) DriverManager.getDriver())
                        .executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
            } catch (Exception ignored) {
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