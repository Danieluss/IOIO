package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioio.jsontools.core.CoreApp;
import com.ioio.jsontools.core.rest.ModifierType;
import com.ioio.jsontools.core.rest.data.JsonFilterData;
import com.ioio.jsontools.core.rest.data.JsonModifiersData;
import com.ioio.jsontools.core.rest.data.ModifierData;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

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

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
    }

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

    private String buildAPIPayload(String json, String filter) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new JsonFilterData(json, filter));
    }

    @Test
    public void shouldShouldMaxifier() {
        basicTestStrategy("{\"obj\": {\"arr\": [{ \"def\": 999}, { \"def\": 112}], \"xyz\": \"value\"}}",
                "{\n  \"obj\" : {\n    \"arr\" : [ {\n      \"def\" : 999\n    }, {\n      \"def\" : 112\n    } ],\n    \"xyz\" : \"value\"\n  }\n}",
                MAXIFY_REST);
    }

    @Test
    public void shouldShouldWhitelist() throws JsonProcessingException {
        basicTestStrategy(buildAPIPayload("{\"some_field\": 123, \"some_other_field\": 1234}",
                "{\"some_field\": true}"),
                "{\"some_field\":123}",
                WHITELIST_REST);
    }

    @Test
    public void shouldShouldBlacklist() throws JsonProcessingException {
        basicTestStrategy(buildAPIPayload("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
                "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}"),
                "{\"some_field\":{\"nested_object2\":{\"nested_object\":456}}}",
                BLACKLIST_REST);
    }

    private void whitelistTest(String json, String filter, String expectedResponse) throws JsonProcessingException {
        basicTestStrategy(
                buildAPIPayload(json, filter),
                expectedResponse, WHITELIST_REST);
    }

    @Test
    public void shouldTranslateLikeWhitelist() throws JsonProcessingException {
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
    public void shouldHandleNestedObjects() throws JsonProcessingException {
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
    public void shouldHandleArrays() throws JsonProcessingException {
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
    public void shouldDiscardEmpty() throws JsonProcessingException {
        whitelistTest("{\"some_field\": []}",
                "{\"some_field\": true}",
                "{}");
    }

    private void blacklistTest(String json, String filter, String expectedResponse) throws JsonProcessingException {
        basicTestStrategy(
                buildAPIPayload(json, filter),
                expectedResponse, BLACKLIST_REST);
    }

    @Test
    public void shouldTranslateLikeBlacklist() throws JsonProcessingException {
        blacklistTest("{\"some_field\": 123, \"some_other_field\": 1234}",
                "{\"some_field\": true}",
                "{\"some_other_field\":1234}");
    }

    @Test
    public void shouldHandleNestedObjectsBlack() throws JsonProcessingException {
        blacklistTest("{\"some_field\": {\"nested_object1\": {\"nested_object\": 123}, \"nested_object2\": {\"nested_object\": 456}}}",
                "{\"some_field\": {\"nested_object1\": {\"nested_object\": true}}}",
                "{\"some_field\":{\"nested_object2\":{\"nested_object\":456}}}");
    }

    @Test
    public void shouldHandleArraysBlack() throws JsonProcessingException {
        blacklistTest("{\"arr\": [{\"x\":123, \"y\":543333}, {\"x\":1, \"y\":2}, {\"x\":-21, \"y\":76}, {\"x\":36, \"y\":0}]}",
                "{\"arr\":{\"__array__\":{\"x\":true}}}",
                "{\"arr\":[{\"y\":543333},{\"y\":2},{\"y\":76},{\"y\":0}]}");
    }

    private void minificationTest(String json, String expectedResponse) {
        basicTestStrategy(json,
                expectedResponse, MINIFY_REST);
    }

    @Test
    public void shouldDoSimpleObjects() throws JsonProcessingException {
        minificationTest("{\n  \"some_object\" : {\n    \"x\" : 964,\n    \"y\" : \"aaaxx\"\n  }\n}",
                "{\"some_object\":{\"x\":964,\"y\":\"aaaxx\"}}");
    }

    @Test
    public void shouldDoArrays() throws JsonProcessingException {
        minificationTest("{\n  \"probably_array\" : [ 1, 2, 3, 4 ]\n}",
                "{\"probably_array\":[1,2,3,4]}");
    }

    @Test
    public void shouldDoEmpty() throws JsonProcessingException {
        minificationTest("{              }", "{}");
    }

    private String buildAPIPayload(String json, List<ModifierData> modifiers) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new JsonModifiersData(json, modifiers));
    }


    private void combinedTest(String json, List<ModifierData> modifiers, String expectedResponse) throws JSONException, JsonProcessingException {
        basicTestStrategy(
                buildAPIPayload(json, modifiers),
                expectedResponse, COMBINED_REST);
    }

    @Test
    public void shouldWhitelistAndMaxify() throws JSONException, JsonProcessingException {
        combinedTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}", Arrays.asList(new ModifierData(ModifierType.whitelist, ""), new ModifierData(ModifierType.maxify, "")), "{ }");
    }

    @Test
    public void shouldNotScrewIndices() throws JSONException, JsonProcessingException {
        combinedTest("{\"some_field\": [1,2,3,4,{\"nested_object\": 123}]}",
                Arrays.asList(
                        new ModifierData(ModifierType.minify, ""),
                        new ModifierData(ModifierType.whitelist, "{\"some_field\": true}"),
                        new ModifierData(ModifierType.maxify, "")), "{\n" +
                        "  \"some_field\" : [ 1, 2, 3, 4, {\n" +
                        "    \"nested_object\" : 123\n" +
                        "  } ]\n" +
                        "}");
    }

}
