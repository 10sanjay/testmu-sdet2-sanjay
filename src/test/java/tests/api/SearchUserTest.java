package tests.api;

import api.ApiClient;
import api.ResponseValidator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.base.BaseTest;

@Epic("API") @Feature("User Search")
public class SearchUserTest extends BaseTest {

    @DataProvider(name = "searchTerms")
    public Object[][] terms() { return new Object[][]{ {"John"}, {"Emily"}, {"Michael"} }; }

    @Test(dataProvider = "searchTerms", description = "Search returns 200 & users array")
    public void testSearchUsers(String term) {
        Response r = ApiClient.getWithQuery("/users/search", "q", term);
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertResponseTimeBelow(r, 3000);
        Assert.assertNotNull(r.jsonPath().getList("users"));
    }

    @Test(description = "Filter by nested key")
    public void testFilterUsers() {
        Response r = ApiClient.get("/users/filter?key=hair.color&value=Brown");
        ResponseValidator.assertStatus(r, 200);
        ResponseValidator.assertContainsField(r, "users");
    }
}