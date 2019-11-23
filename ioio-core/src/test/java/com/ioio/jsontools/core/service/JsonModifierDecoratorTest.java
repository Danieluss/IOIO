package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.filter.Filter;
import com.ioio.jsontools.core.service.filter.JsonFilter;
import com.ioio.jsontools.core.service.maxification.Maxifier;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonModifierDecoratorTest {

    private void testDecorators(JsonModifier jsonModifier, String json, String expectedResponse) throws JsonProcessingException {
        String response = jsonModifier.modify(json);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void maxifyFiltered() throws JsonProcessingException {
        JsonModifier jsonModifier = new Maxifier(new JsonFilter(new JsonModifierImpl(), "{\"obj\": {\"arr\": {\"__array__\": {\"def\": true}}, \"xyz\": true}}", Filter.WHITELIST));
        testDecorators(jsonModifier,
                "{\"obj\": {\"arr\": [{\"abc\": \"some text\", \"def\": 999}, {\"abc\": \"some other text\", \"def\": 112}], \"xyz\": \"value\"}}",
                "{\n  \"obj\" : {\n    \"arr\" : [ {\n      \"def\" : 999\n    }, {\n      \"def\" : 112\n    } ],\n    \"xyz\" : \"value\"\n  }\n}");
    }
}