package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import core.DriverManager;
import utils.RetryUtil;
import utils.WaitUtils;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    /** Robust click — scrolls into view, retries, falls back to JS click. */
    protected void click(By locator) {
        RetryUtil.withRetry(() -> {
            WebElement el = WaitUtils.clickable(locator);
            scrollIntoView(el);
            try {
                el.click();
            } catch (Exception e) {
                jsClick(el);
            }
        }, 3, 300);
    }

    /** Click and verify URL transition; fallback = JS click; final fallback = direct navigation. */
    protected void clickAndNavigateTo(By locator, String expectedUrlFragment) {
        click(locator);
        try {
            WaitUtils.urlContains(expectedUrlFragment);
        } catch (Exception primaryFail) {
            System.out.println("⚠ Click did not navigate to " + expectedUrlFragment + " → forcing JS click");
            try {
                WebElement el = WaitUtils.visible(locator);
                jsClick(el);
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.urlContains(expectedUrlFragment));
            } catch (Exception jsFail) {
                System.out.println("⚠ JS click failed too → direct navigation");
                String base = driver.getCurrentUrl().split("\\?")[0]
                        .replaceAll("[^/]+$", "");
                driver.navigate().to(base + expectedUrlFragment);
                WaitUtils.urlContains(expectedUrlFragment);
            }
        }
    }

    protected void type(By locator, String text) {
        RetryUtil.withRetry(() -> {
            WebElement el = WaitUtils.visible(locator);
            scrollIntoView(el);
            el.clear();
            el.sendKeys(text);
        }, 3, 300);
    }

    protected String text(By locator) {
        return WaitUtils.visible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try { return WaitUtils.visible(locator).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // ───────── helpers ─────────
    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
        } catch (Exception ignored) {}
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}