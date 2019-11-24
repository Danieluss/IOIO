package com.ioio.jsontools.core.service.maxification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierImpl;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MaxifierTest {

    JsonModifier jsonModifier = new Maxifier(new JsonModifierImpl());

    private void maxificationTest(String json, String expectedResponse) throws JsonProcessingException {
        String response = jsonModifier.modify(json);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testSimpleObjects() throws JsonProcessingException {
        maxificationTest("{\"some_field\": 123}",
                "{\n  \"some_field\" : 123\n}");
        maxificationTest("{\"some_object\": {\"x\": 964, \"y\": \"aaaxx\"}}",
                "{\n  \"some_object\" : {\n    \"x\" : 964,\n    \"y\" : \"aaaxx\"\n  }\n}");
    }

    @Test
    public void testArrays() throws JsonProcessingException {
        maxificationTest("{\"probably_array\":[1, 2, 3, 4]}",
                "{\n  \"probably_array\" : [ 1, 2, 3, 4 ]\n}");
        maxificationTest("{\"some_nested_array\":[1, {\"inner_array\": [\"abc\", \"def\"]}, 3, 4]}",
                "{\n  \"some_nested_array\" : [ 1, {\n    \"inner_array\" : [ \"abc\", \"def\" ]\n  }, 3, 4 ]\n}");
    }

    @Test
    public void testEmpty() throws JsonProcessingException {
        maxificationTest("{}", "{ }");
    }
}