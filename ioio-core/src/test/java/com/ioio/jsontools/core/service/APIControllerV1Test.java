package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioio.jsontools.core.CoreApp;
import com.ioio.jsontools.core.service.filter.Filter;
import com.ioio.jsontools.core.service.filter.FilterService;
import com.ioio.jsontools.core.service.minification.Minifier;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.json.JSONException;
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

    public void basicTestStrategy(String payload, String expected, String path) throws org.json.JSONException {
        String result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("http://localhost:" + port + "/api/v1/" + path)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        assertEquals(result, expected);
    }

    public String buildAPIPayload(String json, String filter) throws org.json.JSONException {
        return new JSONObject()
                .put("json", json)
                .put("filter", filter).toString();
    }

    @Test
    public void shouldShouldMaxifier() throws org.json.JSONException {
        basicTestStrategy("{\"obj\": {\"arr\": [{ \"def\": 999}, { \"def\": 112}], \"xyz\": \"value\"}}",
        "{\n  \"obj\" : {\n    \"arr\" : [ {\n      \"def\" : 999\n    }, {\n      \"def\" : 112\n    } ],\n    \"xyz\" : \"value\"\n  }\n}",
                "modifier/maxifier");
    }

    @Test
    public void shouldShouldWhitelist() throws org.json.JSONException {
        basicTestStrategy( buildAPIPayload("{\"some_field\": 123, \"some_other_field\": 1234}",
                "{\"some_field\": true}"),
                "{\"some_field\":123}",
                "filter/whitelist");
    }

    @Test
    public void shouldShouldBlacklist() throws org.json.JSONException {
        basicTestStrategy( buildAPIPayload("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
                "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}"),
                "{\"some_field\":{\"nested_object2\":{\"nested_object\":456}}}",
                "filter/blacklist");
    }
    
    private FilterService filterService = new FilterService(new ObjectMapper(), null);

    private void whitelistTest(String json, String filter, String expectedResponse) throws JsonProcessingException, JSONException {
        String response = filterService.filter(json, filter, Filter.WHITELIST);
        assertEquals(expectedResponse, response);
        basicTestStrategy(
                buildAPIPayload(json, filter),
                expectedResponse, "filter/whitelist");
    }

    @Test
    public void shouldTranslateLikeWhitelist() throws JsonProcessingException, JSONException {
        whitelistTest("{\"some_field\": 123}",
                "{}",
                "{}");
        whitelistTest("{\"some_field\": 123}",
                "{\"some_field\": true}",
                "{\"some_field\":123}");
        whitelistTest("{\"some_field\": 123, \"some_other_field\": 1234}",
                "{\"some_field\": true}",
                "{\"some_field\":123}");
    }

    @Test
    public void shouldHandleNestedObjects() throws JsonProcessingException, JSONException {
        whitelistTest("{\"some_field\": {\"nested_object\": 123}}",
                "{}",
                "{}");
        whitelistTest("{\"some_field\": {\"nested_object\": 123}}",
                "{\"some_field\": {\"nested_object\": true}}",
                "{\"some_field\":{\"nested_object\":123}}");
        whitelistTest("{\"some_field\": {\"nested_object\": 123}}",
                "{\"some_field\": true}",
                "{\"some_field\":{\"nested_object\":123}}");
        whitelistTest("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 123}}}",
                "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}",
                "{\"some_field\":{\"nested_object1\":{\"nested_object\":123}}}");
    }

    @Test
    public void shouldHandleArrays() throws JsonProcessingException, JSONException {
        whitelistTest("{\"some_field\": [1,2,3,4]}",
                "{}",
                "{}");
        whitelistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": {\"__array__\": {\"nested_object\": true}}}",
                "{\"some_field\":[{\"nested_object\":123}]}");
        whitelistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": {\"__array__\": {\"nested_object\": true}}}",
                "{\"some_field\":[{\"nested_object\":123}]}");
        whitelistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": {\"__array__\": {\"nested_object\": true}}}",
                "{\"some_field\":[{\"nested_object\":123}]}");
        whitelistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": {\"__array__\": {\"__leaf__\": true}}}",
                "{\"some_field\":[1,2,3,4]}");

        whitelistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": [true, true, true, true, {\"nested_object\": false}]}",
                "{\"some_field\":[1,2,3,4]}");
        whitelistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": [false, true, false, true, false]}",
                "{\"some_field\":[2,4]}");
        whitelistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": [true, false, true, false, true]}",
                "{\"some_field\":[1,3,{\"nested_object\":123}]}");
        whitelistTest("{\"arr\": [{\"x\":123, \"y\":543333}, {\"x\":1, \"y\":2}, {\"x\":-21, \"y\":76}, {\"x\":36, \"y\":0}]}",
                "{\"arr\":{\"__array__\":{\"x\":true}}}",
                "{\"arr\":[{\"x\":123},{\"x\":1},{\"x\":-21},{\"x\":36}]}");
    }

    @Test
    public void shouldDiscardEmpty() throws JsonProcessingException, JSONException {
        whitelistTest("{\"some_field\": []}",
                "{\"some_field\": true}",
                "{}");
    }

    private void blacklistTest(String json, String filter, String expectedResponse) throws JsonProcessingException, JSONException {
        String response = filterService.filter(json, filter, Filter.BLACKLIST);
        assertEquals(expectedResponse, response);
        basicTestStrategy(
                buildAPIPayload(json, filter),
                expectedResponse, "filter/blacklist");
    }

    @Test
    public void shouldTranslateLikeBlacklist() throws JsonProcessingException, JSONException {
        blacklistTest("{\"some_field\": 123}",
                "{}",
                "{\"some_field\":123}");
        blacklistTest("{\"some_field\": 123}",
                "{\"some_field\": true}",
                "{}");
        blacklistTest("{\"some_field\": 123, \"some_other_field\": 1234}",
                "{\"some_field\": true}",
                "{\"some_other_field\":1234}");
    }

    @Test
    public void shouldHandleNestedObjectsBlack() throws JsonProcessingException, JSONException {
        blacklistTest("{\"some_field\": {\"nested_object\": 123}}",
                "{}",
                "{\"some_field\":{\"nested_object\":123}}");
        blacklistTest("{\"some_field\": {\"nested_object\": 123}}",
                "{\"some_field\": {\"nested_object\": true}}",
                "{}");
        blacklistTest("{\"some_field\": {\"nested_object\": 123}}",
                "{\"some_field\": true}",
                "{}");
        blacklistTest("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
                "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}",
                "{\"some_field\":{\"nested_object2\":{\"nested_object\":456}}}");
    }

    @Test
    public void shouldHandleArraysBlack() throws JsonProcessingException, JSONException {
        blacklistTest("{\"some_field\": [1,2,3,4]}",
                "{}",
                "{\"some_field\":[1,2,3,4]}");
        blacklistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": [false, false, false, false, {\"nested_object\": true}]}",
                "{\"some_field\":[1,2,3,4]}");
        blacklistTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                "{\"some_field\": [true, false, true, false, true]}",
                "{\"some_field\":[2,4]}");
        blacklistTest("{\"arr\": [{\"x\":123, \"y\":543333}, {\"x\":1, \"y\":2}, {\"x\":-21, \"y\":76}, {\"x\":36, \"y\":0}]}",
                "{\"arr\":{\"__array__\":{\"x\":true}}}",
                "{\"arr\":[{\"y\":543333},{\"y\":2},{\"y\":76},{\"y\":0}]}");
    }

    private JsonModifier jsonModifier = new Minifier(new JsonModifierImpl());

    private void minificationTest(String json, String expectedResponse) throws JsonProcessingException, JSONException {
        String response = jsonModifier.modify(json);
        assertEquals(expectedResponse, response);
        basicTestStrategy(json,
                expectedResponse, "modifier/minifier");
    }

    @Test
    public void shouldDoSimpleObjects() throws JsonProcessingException, JSONException {
        minificationTest("{\n  \"some_field\" : 123\n}", "{\"some_field\":123}");
        minificationTest("{\n  \"some_object\" : {\n    \"x\" : 964,\n    \"y\" : \"aaaxx\"\n  }\n}",
                "{\"some_object\":{\"x\":964,\"y\":\"aaaxx\"}}");
    }

    @Test
    public void shouldDoArrays() throws JsonProcessingException, JSONException {
        minificationTest("{\n  \"probably_array\" : [ 1, 2, 3, 4 ]\n}",
                "{\"probably_array\":[1,2,3,4]}");
        minificationTest("{\n  \"some_nested_array\" : [ 1, {\n    \"inner_array\" : [ \"abc\", \"def\" ]\n  }, 3, 4 ]\n}",
                "{\"some_nested_array\":[1,{\"inner_array\":[\"abc\",\"def\"]},3,4]}");
    }

    @Test
    public void shouldDoEmpty() throws JsonProcessingException, JSONException {
        minificationTest("{ }", "{}");
        minificationTest("{              }", "{}");
    }
}
