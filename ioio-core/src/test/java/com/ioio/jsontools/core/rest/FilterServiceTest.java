package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioio.jsontools.core.service.filter.Filter;
import com.ioio.jsontools.core.service.filter.FilterService;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterServiceTest {

    private FilterService filterService = new FilterService(new ObjectMapper(), null);

    private void whitelistTest(String json, String filter, String expectedResponse) throws JsonProcessingException {
        String response = filterService.filter(json, filter, Filter.WHITELIST);
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
    }

    @Test
    public void shouldDiscardEmpty() throws JsonProcessingException {
        whitelistTest("{\"some_field\": []}",
                "{\"some_field\": true}",
                "{}");
    }

}