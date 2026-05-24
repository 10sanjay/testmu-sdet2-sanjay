package api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Thin HTTP client. Two flavours:
 *  - get/post/put/delete         → unauthenticated endpoints
 *  - secureGet/Post/Put/Delete   → attaches Bearer; refreshes & retries once on 401
 */
public final class ApiClient {

    private ApiClient() {}

    // ───────── Unauthenticated ─────────
    public static Response get(String path)                                { return given().spec(RequestSpecBuilder.base()).get(path); }
    public static Response get(String path, RequestSpecification spec)     { return given().spec(spec).get(path); }
    public static Response post(String path, Object body)                  { return given().spec(RequestSpecBuilder.base()).body(body).post(path); }
    public static Response put(String path, Object body)                   { return given().spec(RequestSpecBuilder.base()).body(body).put(path); }
    public static Response delete(String path)                             { return given().spec(RequestSpecBuilder.base()).delete(path); }
    public static Response getWithQuery(String path, String k, String v)   { return given().spec(RequestSpecBuilder.base()).queryParam(k, v).get(path); }

    // ───────── Authenticated (auto-token + auto-refresh) ─────────
    public static Response secureGet(String path)               { return executeSecure("GET",    path, null); }
    public static Response securePost(String path, Object body) { return executeSecure("POST",   path, body); }
    public static Response securePut(String path, Object body)  { return executeSecure("PUT",    path, body); }
    public static Response secureDelete(String path)            { return executeSecure("DELETE", path, null); }

    /** Executes a request with Bearer token; on 401 → refresh token and retry exactly once. */
    private static Response executeSecure(String method, String path, Object body) {
        Response response = call(method, path, body, AuthManager.getToken());

        if (response.statusCode() == 401) {
            System.out.println("⚠ 401 received → refreshing token and retrying " + method + " " + path);
            String newToken = AuthManager.refreshToken();
            response = call(method, path, body, newToken);
        }
        return response;
    }

    private static Response call(String method, String path, Object body, String token) {
        RequestSpecification spec = RequestSpecBuilder.authenticated(token);
        switch (method) {
            case "GET":    return given().spec(spec).get(path);
            case "DELETE": return given().spec(spec).delete(path);
            case "POST":   return given().spec(spec).body(body).post(path);
            case "PUT":    return given().spec(spec).body(body).put(path);
            default:       throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
}