package api;

import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public final class ResponseValidator {

    private ResponseValidator() {}

    public static void assertStatus(Response r, int expected) {
        Assert.assertEquals(r.statusCode(), expected,
                "Unexpected status. Body: " + r.asString());
    }

    public static void assertResponseTimeBelow(Response r, long ms) {
        Assert.assertTrue(r.time() < ms,
                "Slow response: " + r.time() + "ms (SLA: " + ms + "ms)");
    }

    public static void assertSchema(Response r, String classpathSchema) {
        r.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(classpathSchema));
    }

    // 🔧 FIXED: force String overload so TestNG doesn't pick char[] cast
    public static void assertJsonField(Response r, String jsonPath, Object expected) {
        Object actual = r.jsonPath().get(jsonPath);
        String actualStr   = (actual   == null) ? "null" : actual.toString();
        String expectedStr = (expected == null) ? "null" : expected.toString();
        Assert.assertEquals(actualStr, expectedStr,
                "Field '" + jsonPath + "' mismatch");
    }

    public static void assertContainsField(Response r, String jsonPath) {
        Assert.assertNotNull(r.jsonPath().get(jsonPath),
                "Missing field: " + jsonPath);
    }
}