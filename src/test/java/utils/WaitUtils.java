package utils;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import core.DriverManager;
import core.FrameworkConstants;

public final class WaitUtils {

    private WaitUtils() {}

    // ───────── Core wait factory ─────────
    private static WebDriverWait getWait() {
        return getWait(FrameworkConstants.DEFAULT_EXPLICIT_WAIT);
    }

    private static WebDriverWait getWait(int seconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds));
    }

    // ───────── Visibility ─────────
    public static WebElement visible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement visible(By locator, int seconds) {
        return getWait(seconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ───────── Clickability ─────────
    public static WebElement clickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement clickable(By locator, int seconds) {
        return getWait(seconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Wait for multiple elements to be clickable in one call. */
    public static void allClickable(By... locators) {
        for (By l : locators) clickable(l);
    }

    // ───────── URL ─────────
    public static boolean urlContains(String fragment) {
        return getWait().until(ExpectedConditions.urlContains(fragment));
    }

    // ───────── Presence (in DOM, may be hidden) ─────────
    public static WebElement present(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ───────── Invisibility (for spinners/overlays) ─────────
    public static boolean invisible(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ───────── Custom condition ─────────
    public static <T> T forCondition(Function<org.openqa.selenium.WebDriver, T> condition) {
        return getWait().until(condition::apply);
    }

    public static <T> T forCondition(ExpectedCondition<T> condition) {
        return getWait().until(condition);
    }
}