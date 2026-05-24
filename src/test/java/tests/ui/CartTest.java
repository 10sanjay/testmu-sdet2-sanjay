package tests.ui;

import core.ConfigManager;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import tests.base.BaseTest;

@Epic("UI") @Feature("Cart")
public class CartTest extends BaseTest {

    @Test(description = "Add multiple products and validate cart contents")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddItemsToCart() {
        InventoryPage inv = new LoginPage().open()
                .loginAs(ConfigManager.get("ui.username"), ConfigManager.get("ui.password"))
                .addToCart("Sauce Labs Backpack")
                .addToCart("Sauce Labs Bike Light");
        Assert.assertEquals(inv.cartCount(), 2);
        CartPage cart = inv.openCart();
        Assert.assertTrue(cart.itemNames().containsAll(java.util.List.of(
                "Sauce Labs Backpack", "Sauce Labs Bike Light")));
    }

    @Test(description = "Cross-browser smoke: cart operations consistent across browsers")
    public void testCartSmoke() {
        InventoryPage inv = new LoginPage().open()
                .loginAs(ConfigManager.get("ui.username"), ConfigManager.get("ui.password"))
                .addToCart("Sauce Labs Backpack");
        Assert.assertEquals(inv.cartCount(), 1);
    }
}