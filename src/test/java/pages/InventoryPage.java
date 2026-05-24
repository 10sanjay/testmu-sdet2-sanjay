package pages;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
        // 🔑 Ensure first item is fully rendered (React hydration complete)
        WaitUtils.visible(itemNames);
    }

    public boolean isLoaded() { return isDisplayed(container); }

    public List<String> productNames() {
        return driver.findElements(itemNames).stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
    }

    /** Adds product and verifies the action by:
     *  (1) waiting for the button to flip to "Remove"
     *  (2) waiting for badge count to reach expected value */
    public InventoryPage addToCart(String productName) {
        int currentCount = cartCount();
        int expectedCount = currentCount + 1;

        String addKey    = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        String removeKey = "remove-" + productName.toLowerCase().replace(" ", "-");

        By addBtn    = By.cssSelector("[data-test='" + addKey + "']");
        By removeBtn = By.cssSelector("[data-test='" + removeKey + "']");

        click(addBtn);

        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.DEFAULT_EXPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(removeBtn));

        waitForBadgeCount(expectedCount);
        return this;
    }

    public int cartCount() {
        return isDisplayed(cartBadge)
                ? Integer.parseInt(WaitUtils.visible(cartBadge).getText())
                : 0;
    }

    public CartPage openCart() {
        clickAndNavigateTo(cartLink, "cart.html");
        return new CartPage();
    }

    /** Polls the badge until its number matches expected count. */
    private void waitForBadgeCount(int expected) {
        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.DEFAULT_EXPLICIT_WAIT))
                .until(d -> {
                    try {
                        return Integer.parseInt(d.findElement(cartBadge).getText()) == expected;
                    } catch (Exception e) {
                        return false;
                    }
                });
    }
}