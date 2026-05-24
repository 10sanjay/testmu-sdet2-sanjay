package pages;

import org.openqa.selenium.By;

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
        WaitUtils.visible(firstName);
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

    /** Submit empty form — error appears on same page, no navigation. */
    public String submitInvalid() {
        click(continueBtn);
        return WaitUtils.visible(errorMsg).getText();
    }
}