package tests.api;

import api.ApiClient;
import api.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

@Epic("API") @Feature("Authorized Endpoints")
public class SecureEndpointsTest extends BaseTest {

    @Test(description = "GET /auth/products requires & accepts Bearer token")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetProductsAuthorized() {
        Response r = ApiClient.secureGet("/auth/products?limit=5");
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertResponseTimeBelow(r, 3000);
        ResponseValidator.assertContainsField(r, "products");
        Assert.assertFalse(r.jsonPath().getList("products").isEmpty(),
                "Products list should not be empty");
    }

    @Test(description = "GET /auth/carts is secured & returns carts")
    public void testGetCartsAuthorized() {
        Response r = ApiClient.secureGet("/auth/carts");
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertContainsField(r, "carts");
    }

    @Test(description = "Missing Bearer header → 401 from secured endpoint")
    public void testProductsWithoutTokenReturns401() {
        Response r = ApiClient.get("/auth/products");
        Assert.assertEquals(r.statusCode(), 401);
    }
}