package tests.integration;

import api.*;
import core.ScenarioContext;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import tests.base.BaseTest;
import java.util.Map;

@Epic("Integration") @Feature("User Lifecycle")
public class UserLifecycleFlowTest extends BaseTest {

    @Test(description = "Create → Read → Update → Delete user lifecycle")
    @Severity(SeverityLevel.BLOCKER)
    public void testUserLifecycle() {
        // Create
        Map<String,Object> payload = PayloadBuilder.newUserPayload();
        Response create = ApiClient.post("/users/add", payload);
        ResponseValidator.assertStatus(create, 201);
        ScenarioContext.set("userId", create.jsonPath().getInt("id"));

        // Read (DummyJSON returns existing fixture #1)
        Response read = ApiClient.get("/users/1");
        ResponseValidator.assertStatus(read, 200);

        // Update
        Map<String,Object> upd = PayloadBuilder.updateUserPayload();
        Response u = ApiClient.put("/users/1", upd);
        ResponseValidator.assertStatus(u, 200);
        ResponseValidator.assertJsonField(u, "lastName", upd.get("lastName"));

        // Delete
        Response del = ApiClient.delete("/users/1");
        ResponseValidator.assertStatus(del, 200);
        ResponseValidator.assertJsonField(del, "isDeleted", true);
    }
}