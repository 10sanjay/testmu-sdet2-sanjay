package api;

import core.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecBuilder {
    private RequestSpecBuilder() {}

    public static RequestSpecification base() {
        return new io.restassured.builder.RequestSpecBuilder()
                .setBaseUri(ConfigManager.get("api.baseUrl"))
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    public static RequestSpecification authenticated(String token) {
        return new io.restassured.builder.RequestSpecBuilder()
                .setBaseUri(ConfigManager.get("api.baseUrl"))
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}