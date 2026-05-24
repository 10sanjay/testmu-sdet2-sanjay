package pages;

import org.openqa.selenium.By;

import core.ConfigManager;
import utils.WaitUtils;

public class LoginPage extends BasePage {

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginBtn = By.id("login-button");
    private final By errorMsg = By.cssSelector("[data-test='error']");

    public LoginPage open() {
        driver.get(ConfigManager.get("ui.baseUrl"));
        WaitUtils.visible(username);
        return this;
    }

    public InventoryPage loginAs(String u, String p) {
        type(username, u);
        type(password, p);
        click(loginBtn);
        WaitUtils.urlContains("inventory");
        return new InventoryPage();
    }

    public String submitInvalidLogin(String u, String p) {
        type(username, u);
        type(password, p);
        click(loginBtn);
        return WaitUtils.visible(errorMsg).getText();
    }
}