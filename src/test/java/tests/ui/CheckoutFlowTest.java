package tests.ui;

import com.fasterxml.jackson.databind.JsonNode;
import core.ConfigManager;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import tests.base.BaseTest;
import utils.TestDataReader;

@Epic("UI")
@Feature("Checkout")
public class CheckoutFlowTest extends BaseTest {

    @DataProvider(name = "checkoutData")
    public Object[][] checkoutData() {
        JsonNode root = TestDataReader.read("checkout.json");
        Object[][] data = new Object[root.size()][3];
        for (int i = 0; i < root.size(); i++) {
            data[i][0] = root.get(i).get("firstName").asText();
            data[i][1] = root.get(i).get("lastName").asText();
            data[i][2] = root.get(i).get("zip").asText();
        }
        return data;
    }

    @Test(dataProvider = "checkoutData", description = "End-to-end checkout - parameterised")
    @Severity(SeverityLevel.BLOCKER)
    public void testEndToEndCheckout(String first, String last, String zip) {
        InventoryPage inv = new LoginPage().open()
                .loginAs(ConfigManager.get("ui.username"), ConfigManager.get("ui.password"))
                .addToCart("Sauce Labs Backpack");
        CheckoutPage co = inv.openCart().checkout()
                .fillInfo(first, last, zip).continueCheckout();
        Assert.assertTrue(co.isOverviewLoaded());
        Assert.assertEquals(co.finish(), "Thank you for your order!");
    }

    @Test(description = "Checkout form rejects empty mandatory fields")
    public void testCheckoutFormValidation() {
        InventoryPage inv = new LoginPage().open()
                .loginAs(ConfigManager.get("ui.username"), ConfigManager.get("ui.password"))
                .addToCart("Sauce Labs Backpack");

        String err = inv.openCart().checkout().submitInvalid();
        Assert.assertTrue(err.toLowerCase().contains("first name"),
                "Expected 'first name' in error, got: " + err);
    }
}