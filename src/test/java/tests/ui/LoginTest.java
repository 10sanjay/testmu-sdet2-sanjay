package tests.ui;

import core.ConfigManager;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import tests.base.BaseTest;

@Epic("UI") @Feature("Authentication")
public class LoginTest extends BaseTest {

    @Test(description = "Valid login lands on inventory")
    @Severity(SeverityLevel.BLOCKER)
    public void testValidLogin() {
        InventoryPage inv = new LoginPage().open()
                .loginAs(ConfigManager.get("ui.username"), ConfigManager.get("ui.password"));
        Assert.assertTrue(inv.isLoaded());
        Assert.assertFalse(inv.productNames().isEmpty());
    }

    @Test(description = "Locked-out user is rejected")
    @Severity(SeverityLevel.CRITICAL)
    public void testLockedOutUser() {
        String err = new LoginPage().open().submitInvalidLogin("locked_out_user", "secret_sauce");
        Assert.assertTrue(err.toLowerCase().contains("locked"));
    }

    @Test(description = "Empty credentials show validation error")
    public void testEmptyCredentials() {
        String err = new LoginPage().open().submitInvalidLogin("", "");
        Assert.assertTrue(err.toLowerCase().contains("username is required"));
    }
}