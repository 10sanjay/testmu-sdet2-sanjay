package tests.api;

import api.*;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import tests.base.BaseTest;

import java.util.Map;

@Epic("API") @Feature("User CRUD")
public class UserCrudTest extends BaseTest {

    @Test(description = "GET /auth/users requires Bearer token")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUsersAuthorized() {
        Response r = ApiClient.secureGet("/auth/users?limit=3");
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertContainsField(r, "users");
    }

    @Test(description = "Read public user by id")
    public void testGetUserById() {
        Response r = ApiClient.get("/users/1");
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertContainsField(r, "firstName");
        ResponseValidator.assertContainsField(r, "email");
    }

    @Test(description = "Create user with dynamic payload (mock persistence)")
    public void testCreateUser() {
        Map<String,Object> payload = PayloadBuilder.newUserPayload();
        Response r = ApiClient.post("/users/add", payload);
        ResponseValidator.assertStatus(r, 201);
        ResponseValidator.assertContainsField(r, "id");
        ResponseValidator.assertJsonField(r, "firstName", payload.get("firstName"));
    }

    @Test(description = "Update user — DummyJSON returns mock-updated values")
    public void testUpdateUser() {
        Map<String,Object> payload = PayloadBuilder.updateUserPayload();
        Response r = ApiClient.put("/users/1", payload);
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertJsonField(r, "lastName", payload.get("lastName"));
    }

    @Test(description = "Delete user returns isDeleted=true")
    public void testDeleteUser() {
        Response r = ApiClient.delete("/users/1");
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertJsonField(r, "isDeleted", true);
    }

    @Test(description = "Non-existent user returns 404")
    public void testUserNotFound() {
        Response r = ApiClient.get("/users/9999999");
        ResponseValidator.assertStatus(r, 404);
    }
}