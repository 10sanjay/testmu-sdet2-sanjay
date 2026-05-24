package api;

import core.ConfigManager;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Centralised token lifecycle for DummyJSON:
 *  - Login → accessToken + refreshToken
 *  - Refresh → new accessToken when expired
 *  - Cache → thread-safe, lazily initialised
 *  - Invalidate → forces re-login on next call
 */
public final class AuthManager {

    private static volatile String accessToken;
    private static volatile String refreshToken;

    private AuthManager() {}

    /** Returns a valid access token, logging in lazily if needed. */
    public static synchronized String getToken() {
        if (accessToken == null) {
            login(ConfigManager.get("api.username"), ConfigManager.get("api.password"));
        }
        return accessToken;
    }

    /** Performs login and caches both tokens. */
    public static synchronized void login(String username, String password) {
        Response r = given().spec(RequestSpecBuilder.base())
                .body(PayloadBuilder.loginPayload(username, password))
                .post("/auth/login");

        if (r.statusCode() != 200) {
            throw new RuntimeException("Auth login failed: " + r.statusCode() + " → " + r.asString());
        }
        accessToken  = r.jsonPath().getString("accessToken");
        refreshToken = r.jsonPath().getString("refreshToken");

        if (accessToken == null) accessToken = r.jsonPath().getString("token"); // legacy compat
        System.out.println("🔐 New access token acquired");
    }

    /** Calls /auth/refresh to mint a new access token without re-entering credentials. */
    public static synchronized String refreshToken() {
        if (refreshToken == null) {
            // No refresh token cached → fall back to full login
            login(ConfigManager.get("api.username"), ConfigManager.get("api.password"));
            return accessToken;
        }
        Response r = given().spec(RequestSpecBuilder.base())
                .body(java.util.Map.of("refreshToken", refreshToken, "expiresInMins", 30))
                .post("/auth/refresh");

        if (r.statusCode() != 200) {
            // Refresh failed → reset and re-login
            invalidate();
            login(ConfigManager.get("api.username"), ConfigManager.get("api.password"));
            return accessToken;
        }
        accessToken = r.jsonPath().getString("accessToken");
        System.out.println("🔄 Access token refreshed");
        return accessToken;
    }

    /** Clears cached tokens — forces a fresh login on the next call. */
    public static synchronized void invalidate() {
        accessToken  = null;
        refreshToken = null;
    }
}