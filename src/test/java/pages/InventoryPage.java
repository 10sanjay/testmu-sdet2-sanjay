package pages;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import core.FrameworkConstants;
import utils.WaitUtils;

public class InventoryPage extends BasePage {

    private final By container = By.id("inventory_container");
    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By cartLink  = By.id("shopping_cart_container");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");

    public InventoryPage() {
        WaitUtils.urlContains("inventory");
        WaitUtils.visible(container);
        WaitUtils.visible(itemNames);   // ensure React hydration done
    }

    public boolean isLoaded() { return isDisplayed(container); }

    public List<String> productNames() {
        return driver.findElements(itemNames).stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
    }

    /**
     * Adds a product and verifies the action via TWO signals:
     *  1. The product's specific Add button flips to Remove → click registered
     *  2. The cart badge reaches the expected count → state propagated
     *
     * This double-check eliminates the "silent click loss" issue on CI runners
     * where React re-renders can race with the next interaction.
     */
    public InventoryPage addToCart(String productName) {
        String slug = productName.toLowerCase().replace(" ", "-");
        By addBtn    = By.cssSelector("[data-test='add-to-cart-" + slug + "']");
        By removeBtn = By.cssSelector("[data-test='remove-" + slug + "']");

        int expectedCount = cartCount() + 1;

        // 🔑 Make sure the Add button is fully ready (visible + clickable + in viewport)
        WaitUtils.clickable(addBtn);

        click(addBtn);

        // 🔑 PRIMARY signal: this specific button transitioned to Remove
        WaitUtils.visible(removeBtn);

        // 🔑 SECONDARY signal: cart badge reflects the new count
        waitForBadgeCount(expectedCount);

        return this;
    }

    public int cartCount() {
        try {
            String text = driver.findElement(cartBadge).getText().trim();
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage openCart() {
        clickAndNavigateTo(cartLink, "cart.html");
        return new CartPage();
    }

    /** Polls badge until it equals the expected count (or empty when expected=0). */
    private void waitForBadgeCount(int expected) {
        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.DEFAULT_EXPLICIT_WAIT))
                .until(d -> cartCount() == expected);
    }
}