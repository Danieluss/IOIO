package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioio.jsontools.core.service.filter.FilterStrategy;
import com.ioio.jsontools.core.service.filter.JsonFilter;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonFilterDataConfigStrategyServiceTest {

    private JsonFilter blacklist = new JsonFilter(new ObjectMapper(), FilterStrategy.BLACKLIST);
    private JsonFilter whitelist = new JsonFilter(new ObjectMapper(), FilterStrategy.WHITELIST);

    private void whitelistTest(String json, String filter, String expectedResponse) throws JsonProcessingException {
        String response = whitelist.filter(json, filter);
        assertEquals(expectedResponse, response);
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
        String response = blacklist.filter(json, filter);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldTranslateLikeBlacklist() throws JsonProcessingException {
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
    public void shouldHandleNestedObjectsBlack() throws JsonProcessingException {
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
    public void shouldHandleArraysBlack() throws JsonProcessingException {
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

}