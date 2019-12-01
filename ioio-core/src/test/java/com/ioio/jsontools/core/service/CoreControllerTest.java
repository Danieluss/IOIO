package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.CoreApp;
import com.ioio.jsontools.core.rest.AvailableModifier;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ioio.jsontools.core.rest.CoreRestDescriptor.*;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoreApp.class}, webEnvironment = RANDOM_PORT)
public class CoreControllerTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void shouldSayPongForOurPing() {
        get("http://localhost:" + port + CORE_BASE + PING_REST)
                .then()
                .assertThat()
                .statusCode(200)
                .body(equalTo("Server responded properly."));
    }

    private void basicTestStrategy(String payload, String expected, String path) {
        String result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("http://localhost:" + port + CORE_BASE + path)
                .then()
                .statusCode(200)
                .extract()
                .asString();

        assertEquals(result, expected);
    }

    private String buildAPIPayload(String json, String filter) throws org.json.JSONException {
        return new JSONObject()
                .put("json", json)
                .put("filter", filter).toString();
    }

    @Test
    public void shouldShouldMaxifier() throws org.json.JSONException {
        basicTestStrategy("{\"obj\": {\"arr\": [{ \"def\": 999}, { \"def\": 112}], \"xyz\": \"value\"}}",
                "{\n  \"obj\" : {\n    \"arr\" : [ {\n      \"def\" : 999\n    }, {\n      \"def\" : 112\n    } ],\n    \"xyz\" : \"value\"\n  }\n}",
                MAXIFY_REST);
    }

    @Test
    public void shouldShouldWhitelist() throws org.json.JSONException {
        basicTestStrategy(buildAPIPayload("{\"some_field\": 123, \"some_other_field\": 1234}",
                "{\"some_field\": true}"),
                "{\"some_field\":123}",
                WHITELIST_REST);
    }

    @Test
    public void shouldShouldBlacklist() throws org.json.JSONException {
        basicTestStrategy(buildAPIPayload("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
                "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}"),
                "{\"some_field\":{\"nested_object2\":{\"nested_object\":456}}}",
                BLACKLIST_REST);
    }

    private void whitelistTest(String json, String filter, String expectedResponse) throws JSONException {
        basicTestStrategy(
                buildAPIPayload(json, filter),
                expectedResponse, WHITELIST_REST);
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

    private void blacklistTest(String json, String filter, String expectedResponse) throws JSONException {
        basicTestStrategy(
                buildAPIPayload(json, filter),
                expectedResponse, BLACKLIST_REST);
    }

    @Test
    public void shouldTranslateLikeBlacklist() throws JsonProcessingException, JSONException {
        blacklistTest("{\"some_field\": 123, \"some_other_field\": 1234}",
                "{\"some_field\": true}",
                "{\"some_other_field\":1234}");
    }

    @Test
    public void shouldHandleNestedObjectsBlack() throws JsonProcessingException, JSONException {
        blacklistTest("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
                "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}",
                "{\"some_field\":{\"nested_object2\":{\"nested_object\":456}}}");
    }

    @Test
    public void shouldHandleArraysBlack() throws JsonProcessingException, JSONException {
        blacklistTest("{\"arr\": [{\"x\":123, \"y\":543333}, {\"x\":1, \"y\":2}, {\"x\":-21, \"y\":76}, {\"x\":36, \"y\":0}]}",
                "{\"arr\":{\"__array__\":{\"x\":true}}}",
                "{\"arr\":[{\"y\":543333},{\"y\":2},{\"y\":76},{\"y\":0}]}");
    }

    private void minificationTest(String json, String expectedResponse) throws JSONException {
        basicTestStrategy(json,
                expectedResponse, MINIFY_REST);
    }

    @Test
    public void shouldDoSimpleObjects() throws JsonProcessingException, JSONException {
        minificationTest("{\n  \"some_object\" : {\n    \"x\" : 964,\n    \"y\" : \"aaaxx\"\n  }\n}",
                "{\"some_object\":{\"x\":964,\"y\":\"aaaxx\"}}");
    }

    @Test
    public void shouldDoArrays() throws JsonProcessingException, JSONException {
        minificationTest("{\n  \"probably_array\" : [ 1, 2, 3, 4 ]\n}",
                "{\"probably_array\":[1,2,3,4]}");
    }

    @Test
    public void shouldDoEmpty() throws JsonProcessingException, JSONException {
        minificationTest("{              }", "{}");
    }

    private String buildAPIPayload(String json, List<AvailableModifier> modifiers, List<String> params) throws org.json.JSONException {
        return new JSONObject()
                .put("json", json)
                .put("modifiers", new JSONArray(modifiers.stream().map(Enum::toString).collect(Collectors.toList())))
                .put("params", new JSONArray(params))
                .toString();
    }


    private void combinedTest(String json, List<AvailableModifier> modifiers, List<String> params, String expectedResponse) throws JSONException {
        basicTestStrategy(
                buildAPIPayload(json, modifiers, params),
                expectedResponse, COMBINED_REST);
    }

    @Test
    public void shouldWhitelistAndMaxify() throws JSONException {
        combinedTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}", Arrays.asList(AvailableModifier.WHITELIST, AvailableModifier.MAXIFY), Arrays.asList("", ""), "{ }");
    }

    @Test
    public void shouldNotFuckupIndices() throws JSONException {
        combinedTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}", Arrays.asList(AvailableModifier.MINIFY, AvailableModifier.WHITELIST, AvailableModifier.MAXIFY), Arrays.asList("", "{\"some_field\": true}", ""), "{\n" +
                "  \"some_field\" : [ 1, 2, 3, 4, {\n" +
                "    \"nested_object\" : 123\n" +
                "  } ]\n" +
                "}");
    }

}
