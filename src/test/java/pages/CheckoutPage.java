package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class CheckoutPage extends BasePage {

    private final By firstName    = By.id("first-name");
    private final By lastName     = By.id("last-name");
    private final By postalCode   = By.id("postal-code");
    private final By continueBtn  = By.id("continue");
    private final By errorMsg     = By.cssSelector("[data-test='error']");
    private final By summaryTotal = By.cssSelector(".summary_total_label");
    private final By finishBtn    = By.id("finish");
    private final By completeHdr  = By.cssSelector("[data-test='complete-header']");

    public CheckoutPage() {
        WaitUtils.urlContains("checkout-step-one.html");
        WaitUtils.allClickable(firstName, lastName, continueBtn);
    }

    public CheckoutPage fillInfo(String f, String l, String zip) {
        type(firstName, f);
        type(lastName, l);
        type(postalCode, zip);
        return this;
    }

    public CheckoutPage continueCheckout() {
        clickAndNavigateTo(continueBtn, "checkout-step-two.html");
        WaitUtils.visible(summaryTotal);
        return this;
    }

    public boolean isOverviewLoaded() {
        return isDisplayed(summaryTotal);
    }

    public String finish() {
        clickAndNavigateTo(finishBtn, "checkout-complete.html");
        return WaitUtils.visible(completeHdr).getText();
    }

    /**
     * Submit empty form to validate field-level error handling.
     *
     * SauceDemo uses React — its internal field state stays `undefined`
     * until a field is touched, which silently bypasses validation.
     * We "touch" each field (type+delete) so React state becomes `""`,
     * THEN submit so validation runs and renders the error element.
     */
    public String submitInvalid() {
        touchAndClear(firstName);
        touchAndClear(lastName);
        touchAndClear(postalCode);

        // Click continue normally — React state is now properly "empty"
        click(continueBtn);

        try {
            return WaitUtils.visible(errorMsg).getText();
        } catch (Exception clickFailed) {
            // Fallback: submit form via JS (skips browser HTML5 validation)
            submitFormViaJs();
            try {
                return WaitUtils.visible(errorMsg).getText();
            } catch (Exception jsFailed) {
                throw new RuntimeException(diagnose("Error message did not appear"), jsFailed);
            }
        }
    }

    // ───────── private helpers ─────────

    /** Forces React to register the field as 'touched with empty value'. */
    private void touchAndClear(By locator) {
        WebElement el = WaitUtils.visible(locator);
        el.sendKeys("x");
        el.sendKeys(Keys.BACK_SPACE);
    }

    /** Programmatic form submission — fires onSubmit handler regardless of click events. */
    private void submitFormViaJs() {
        ((JavascriptExecutor) driver).executeScript(
                "var form = arguments[0].closest('form');" +
                "if (form && form.requestSubmit) { form.requestSubmit(); }" +
                "else if (form) { form.submit(); }",
                WaitUtils.visible(firstName)
        );
    }

    private String diagnose(String reason) {
        WebElement fn = WaitUtils.visible(firstName);
        WebElement ln = WaitUtils.visible(lastName);
        WebElement zip = WaitUtils.visible(postalCode);
        boolean errorInDom = driver.getPageSource().contains("data-test=\"error\"");
        return String.format(
                "%s%n  URL: %s%n  Error element in DOM: %s%n  Fields: first='%s' last='%s' zip='%s'",
                reason,
                driver.getCurrentUrl(),
                errorInDom,
                fn.getAttribute("value"),
                ln.getAttribute("value"),
                zip.getAttribute("value")
        );
    }
}