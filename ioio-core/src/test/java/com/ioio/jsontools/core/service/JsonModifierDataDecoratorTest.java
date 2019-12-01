package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioio.jsontools.core.service.filter.FilterStrategy;
import com.ioio.jsontools.core.service.filter.JsonFilter;
import com.ioio.jsontools.core.service.filter.JsonFilterModifier;
import com.ioio.jsontools.core.service.whitespace.JsonMaxifier;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonModifierDataDecoratorTest {

    private void testDecorators(JsonModifier jsonModifier, String json, String expectedResponse) throws JsonProcessingException {
        String response = jsonModifier.modify(json);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldMaxifyFiltered() throws JsonProcessingException {
        JsonModifier jsonModifier = new JsonMaxifier(new JsonFilterModifier(new JsonModifierImpl(), "{\"obj\": {\"arr\": {\"__array__\": {\"def\": true}}, \"xyz\": true}}", new JsonFilter(new ObjectMapper(), FilterStrategy.WHITELIST)));
        testDecorators(jsonModifier,
                "{\"obj\": {\"arr\": [{\"abc\": \"some text\", \"def\": 999}, {\"abc\": \"some other text\", \"def\": 112}], \"xyz\": \"value\"}}",
                "{\n  \"obj\" : {\n    \"arr\" : [ {\n      \"def\" : 999\n    }, {\n      \"def\" : 112\n    } ],\n    \"xyz\" : \"value\"\n  }\n}");
    }
}