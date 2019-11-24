package com.ioio.jsontools.core.service.minification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierImpl;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinifierTest {

    private JsonModifier jsonModifier = new Minifier(new JsonModifierImpl());

    private void minificationTest(String json, String expectedResponse) throws JsonProcessingException {
        String response = jsonModifier.modify(json);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testSimpleObjects() throws JsonProcessingException {
        minificationTest("{\n  \"some_field\" : 123\n}", "{\"some_field\":123}");
        minificationTest("{\n  \"some_object\" : {\n    \"x\" : 964,\n    \"y\" : \"aaaxx\"\n  }\n}",
                "{\"some_object\":{\"x\":964,\"y\":\"aaaxx\"}}");
    }

    @Test
    public void testArrays() throws JsonProcessingException {
        minificationTest("{\n  \"probably_array\" : [ 1, 2, 3, 4 ]\n}",
                "{\"probably_array\":[1,2,3,4]}");
        minificationTest("{\n  \"some_nested_array\" : [ 1, {\n    \"inner_array\" : [ \"abc\", \"def\" ]\n  }, 3, 4 ]\n}",
                "{\"some_nested_array\":[1,{\"inner_array\":[\"abc\",\"def\"]},3,4]}");
    }

    @Test
    public void testEmpty() throws JsonProcessingException {
        minificationTest("{ }", "{}");
        minificationTest("{              }", "{}");
    }
}
