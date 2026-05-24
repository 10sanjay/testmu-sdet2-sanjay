package pages;

import org.openqa.selenium.By;
import utils.WaitUtils;

public class CheckoutPage extends BasePage {

    // Step 1 — Your Information
    private final By firstName    = By.cssSelector("[data-test='firstName']");
    private final By lastName     = By.cssSelector("[data-test='lastName']");
    private final By postalCode   = By.cssSelector("[data-test='postalCode']");
    private final By continueBtn  = By.cssSelector("[data-test='continue']");
    private final By errorMsg     = By.cssSelector("[data-test='error']");

    // Step 2 — Overview
    private final By summaryTotal = By.cssSelector(".summary_total_label");
    private final By finishBtn    = By.cssSelector("[data-test='finish']");

    // Complete
    private final By completeHdr  = By.cssSelector("[data-test='complete-header']");

    /** Wait for Step-1 page to fully render before any interaction. */
    public CheckoutPage() {
        WaitUtils.urlContains("checkout-step-one");
        WaitUtils.visible(firstName);
    }

    public CheckoutPage fillInfo(String f, String l, String zip) {
        type(firstName, f);
        type(lastName, l);
        type(postalCode, zip);
        return this;
    }

    public CheckoutPage continueCheckout() {
        click(continueBtn);
        WaitUtils.urlContains("checkout-step-two");
        WaitUtils.visible(summaryTotal);
        return this;
    }

    public boolean isOverviewLoaded() {
        return isDisplayed(summaryTotal);
    }

    public String finish() {
        click(finishBtn);
        WaitUtils.urlContains("checkout-complete");
        return WaitUtils.visible(completeHdr).getText();
    }

    /** Click continue with empty form; returns shown error text. */
    public String submitInvalid() {
        click(continueBtn);
        return WaitUtils.visible(errorMsg).getText();
    }
}