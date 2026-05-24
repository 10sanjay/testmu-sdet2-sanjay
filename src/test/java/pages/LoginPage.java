package pages;

import core.ConfigManager;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginBtn = By.id("login-button");
    private final By errorMsg = By.cssSelector("[data-test='error']");

    public LoginPage open() { driver.get(ConfigManager.get("ui.baseUrl")); return this; }

    public InventoryPage loginAs(String u, String p) {
        type(username, u); type(password, p); click(loginBtn);
        return new InventoryPage();
    }
    public String submitInvalidLogin(String u, String p) {
        type(username, u); type(password, p); click(loginBtn);
        return text(errorMsg);
    }
}