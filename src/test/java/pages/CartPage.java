package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import utils.WaitUtils;

public class CartPage extends BasePage {

    private final By container   = By.id("cart_contents_container");
    private final By items       = By.cssSelector(".cart_item .inventory_item_name");
    private final By checkoutBtn = By.id("checkout");

    public CartPage() {
        WaitUtils.urlContains("cart.html");
        WaitUtils.visible(container);
    }

    public List<String> itemNames() {
        return driver.findElements(items).stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
    }

    
    public CheckoutPage checkout() {
        clickAndNavigateTo(checkoutBtn, "checkout-step-one.html");
        return new CheckoutPage();
    }
}