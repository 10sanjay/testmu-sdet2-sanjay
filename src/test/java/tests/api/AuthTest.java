package tests.api;

import api.*;
import core.ConfigManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

@Epic("API") @Feature("Authentication & Authorization")
public class AuthTest extends BaseTest {

    @Test(description = "Valid login returns access & refresh tokens, meets SLA")
    @Severity(SeverityLevel.BLOCKER)
    public void testValidLoginReturnsToken() {
        Response r = ApiClient.post("/auth/login",
                PayloadBuilder.loginPayload(
                        ConfigManager.get("api.username"),
                        ConfigManager.get("api.password")));
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertResponseTimeBelow(r, 3000);
        ResponseValidator.assertContainsField(r, "accessToken");
        ResponseValidator.assertContainsField(r, "refreshToken");
    }

    @Test(description = "Invalid credentials return 4xx")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidLogin() {
        Response r = ApiClient.post("/auth/login",
                PayloadBuilder.loginPayload("invalid_user", "wrong_pass"));
        Assert.assertTrue(r.statusCode() >= 400 && r.statusCode() < 500,
                "Expected 4xx, got " + r.statusCode());
    }

    @Test(description = "Secured endpoint /auth/me succeeds with valid Bearer token")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetMeWithToken() {
        Response r = ApiClient.secureGet("/auth/me");
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertContainsField(r, "id");
        ResponseValidator.assertContainsField(r, "username");
    }

    @Test(description = "Secured endpoint without token returns 401")
    @Severity(SeverityLevel.CRITICAL)
    public void testSecuredEndpointWithoutTokenFails() {
        Response r = ApiClient.get("/auth/me");                  // no Bearer
        Assert.assertEquals(r.statusCode(), 401, "Expected 401 Unauthorized");
    }

    @Test(description = "Refresh-token flow returns a new access token")
    @Severity(SeverityLevel.CRITICAL)
    public void testTokenRefreshFlow() {
        AuthManager.getToken();                  // ensure logged in
        String refreshed = AuthManager.refreshToken();
        Assert.assertNotNull(refreshed, "Refreshed token must not be null");

        Response r = ApiClient.secureGet("/auth/me");
        ResponseValidator.assertStatus(r, 200);
    }

    @Test(description = "After invalidating the token, secure call re-authenticates transparently")
    @Severity(SeverityLevel.CRITICAL)
    public void testAutoReauthAfterInvalidation() {
        AuthManager.invalidate();                // simulate expired/lost token
        Response r = ApiClient.secureGet("/auth/me");
        ResponseValidator.assertStatus(r, 200);  // succeeds because of lazy re-login
    }
}