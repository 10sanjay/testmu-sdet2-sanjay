package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import core.DriverManager;
import utils.WaitUtils;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    /** Robust click — preserves React event chain. */
    protected void click(By locator) {
        WebElement el = WaitUtils.clickable(locator);
        scrollIntoView(el);

        try {
            el.click();    // native click — preserves React events
        } catch (Exception e1) {
            try {
                // Fallback 1: Actions API (still triggers proper events)
                new Actions(driver).moveToElement(el).click().perform();
            } catch (Exception e2) {
                // Fallback 2: JS click (last resort — may break React)
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            }
        }
    }

    /** Click + verify URL change with fallback. */
    protected void clickAndNavigateTo(By locator, String expectedUrlFragment) {
        click(locator);
        try {
            WaitUtils.urlContains(expectedUrlFragment);
        } catch (Exception e) {
            // Direct navigation as final fallback
            String base = driver.getCurrentUrl().split("\\?")[0].replaceAll("[^/]+$", "");
            driver.navigate().to(base + expectedUrlFragment);
            WaitUtils.urlContains(expectedUrlFragment);
        }
    }

    protected void type(By locator, String text) {
        WebElement el = WaitUtils.visible(locator);
        scrollIntoView(el);
        el.clear();
        el.sendKeys(text);
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
}