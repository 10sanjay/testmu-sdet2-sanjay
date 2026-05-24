package tests.integration;

import api.ApiClient;
import api.PayloadBuilder;
import api.ResponseValidator;
import core.ConfigManager;
import core.ScenarioContext;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import tests.base.BaseTest;
import java.util.Map;

@Epic("Integration") @Feature("API → UI")
public class ApiToUiE2ETest extends BaseTest {

    @Test(description = "Create user via API → share via ScenarioContext → validate UI flow")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUserViaApiThenValidateUi() {
        // 1) API: create user
        Map<String,Object> payload = PayloadBuilder.newUserPayload();
        Response r = ApiClient.post("/users/add", payload);
        ResponseValidator.assertStatus(r, 201);
        ScenarioContext.set("createdUserId",   r.jsonPath().getInt("id"));
        ScenarioContext.set("createdUserName", r.jsonPath().getString("firstName"));

        // 2) UI: login flow on saucedemo
        InventoryPage inv = new LoginPage().open()
                .loginAs(ConfigManager.get("ui.username"), ConfigManager.get("ui.password"));
        Assert.assertTrue(inv.isLoaded());

        // 3) Cross validation
        Assert.assertNotNull(ScenarioContext.get("createdUserId"));
        Assert.assertEquals(ScenarioContext.<String>get("createdUserName"), payload.get("firstName"));
    }
}