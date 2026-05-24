package pages;

import core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.RetryUtil;
import utils.WaitUtils;

public abstract class BasePage {
    protected final WebDriver driver;
    protected BasePage() { this.driver = DriverManager.getDriver(); }

    protected void click(By l) {
        RetryUtil.withRetry(() -> WaitUtils.clickable(l).click(), 3, 300);
    }
    protected void type(By l, String text) {
        RetryUtil.withRetry(() -> {
            WebElement e = WaitUtils.visible(l);
            e.clear(); e.sendKeys(text);
        }, 3, 300);
    }
    protected String text(By l) { return WaitUtils.visible(l).getText(); }
    protected boolean isDisplayed(By l) {
        try { return WaitUtils.visible(l).isDisplayed(); } catch (Exception e) { return false; }
    }
}