package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import utils.WaitUtils;

public class InventoryPage extends BasePage {

    private final By container = By.id("inventory_container");
    private final By itemNames = By.cssSelector("[data-test='inventory-item-name']");
    private final By cartLink  = By.id("shopping_cart_container");  
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");

    public InventoryPage() {
        WaitUtils.urlContains("inventory");
        WaitUtils.visible(container);
    }

    public boolean isLoaded() {
        return isDisplayed(container);
    }

    public List<String> productNames() {
        return driver.findElements(itemNames).stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
    }

    public InventoryPage addToCart(String productName) {
        String key = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        click(By.cssSelector("[data-test='" + key + "']"));
        // 🔑 wait for badge so we KNOW the item was added before navigating
        WaitUtils.visible(cartBadge);
        return this;
    }

    public int cartCount() {
        return isDisplayed(cartBadge)
                ? Integer.parseInt(WaitUtils.visible(cartBadge).getText())
                : 0;
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }
}