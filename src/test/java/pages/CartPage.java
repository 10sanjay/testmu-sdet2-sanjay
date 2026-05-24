package pages;

import org.openqa.selenium.By;
import utils.WaitUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private final By container   = By.cssSelector("[data-test='cart-list']");
    private final By items       = By.cssSelector(".cart_item [data-test='inventory-item-name']");
    private final By checkoutBtn = By.cssSelector("[data-test='checkout']");

    public CartPage() {
        WaitUtils.urlContains("cart");
        WaitUtils.visible(container);
    }

    public List<String> itemNames() {
        return driver.findElements(items).stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
    }

    public CheckoutPage checkout() {
        click(checkoutBtn);
        return new CheckoutPage();
    }
}