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

    /** Click add-to-cart and wait for badge to reach expected count. */
    public InventoryPage addToCart(String productName) {
        int expectedCount = cartCount() + 1;
        String key = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(By.cssSelector("[data-test='" + key + "']"));
        waitForBadgeCount(expectedCount);
        return this;
    }

    public int cartCount() {
        try {
            return Integer.parseInt(driver.findElement(cartBadge).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage openCart() {
        clickAndNavigateTo(cartLink, "cart.html");
        return new CartPage();
    }

    /** Polls badge until expected count is reached. */
    private void waitForBadgeCount(int expected) {
        new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.DEFAULT_EXPLICIT_WAIT))
                .until(d -> {
                    try {
                        return Integer.parseInt(d.findElement(cartBadge).getText()) == expected;
                    } catch (Exception ignored) {
                        return false;
                    }
                });
    }
}