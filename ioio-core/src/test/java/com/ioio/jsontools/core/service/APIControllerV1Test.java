package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.CoreApp;
import com.ioio.jsontools.core.service.filter.Filter;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CoreApp.class }, webEnvironment = RANDOM_PORT)
public class APIControllerV1Test {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void shouldSayPongForOurPing() {
        get("http://localhost:" + port + "/api/ping")
                .then()
                .assertThat()
                .statusCode(200)
                .body(equalTo("Server responded properly."));
    }

    @Test
    public void shouldShouldMaxifier() {
        String payload = "{\"obj\": {\"arr\": [{ \"def\": 999}, { \"def\": 112}], \"xyz\": \"value\"}}";
        String expected = "{\n  \"obj\" : {\n    \"arr\" : [ {\n      \"def\" : 999\n    }, {\n      \"def\" : 112\n    } ],\n    \"xyz\" : \"value\"\n  }\n}";

        String result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("http://localhost:" + port + "/api/v1/modifier/maxifier")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        assertEquals(result, expected);
    }

    private String buildAPIRequest(String json, String filter) throws org.json.JSONException {
        return new JSONObject()
                .put("json", json)
                .put("filter", filter).toString();
    }

    @Test
    public void shouldShouldWhitelist() throws org.json.JSONException {
        String payload = buildAPIRequest("{\"some_field\": 123, \"some_other_field\": 1234}", "{\"some_field\": true}");
        String expected = "{\"some_field\":123}";

        String result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("http://localhost:" + port + "/api/v1/filter/whitelist")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        assertEquals(result, expected);
    }

    @Test
    public void shouldShouldBlacklist() throws org.json.JSONException {
        String payload = buildAPIRequest("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}", "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}");
        String expected = "{\"some_field\":{\"nested_object2\":{\"nested_object\":456}}}";

        String result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("http://localhost:" + port + "/api/v1/filter/blacklist")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        assertEquals(result, expected);
    }
}
