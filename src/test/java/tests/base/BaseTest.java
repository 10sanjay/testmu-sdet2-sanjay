package tests.base;

import core.ConfigManager;
import core.DriverManager;
import core.ScenarioContext;
import io.restassured.RestAssured;
import listeners.TestListener;
import org.testng.annotations.*;

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
            String b = browser == null || browser.isBlank() ? ConfigManager.get("browser.name") : browser;
            DriverManager.initDriver(b);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (requiresBrowser()) DriverManager.quitDriver();
        ScenarioContext.clear();
    }

    /** True if the test package needs a browser. API tests skip driver init. */
    protected boolean requiresBrowser() {
        String pkg = this.getClass().getPackage().getName();
        return !pkg.endsWith(".api");
    }
}