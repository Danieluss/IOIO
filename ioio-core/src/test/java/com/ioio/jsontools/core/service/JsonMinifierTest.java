package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioio.jsontools.core.service.whitespace.JsonMinifier;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JsonMinifierTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private JsonModifier jsonModifier = new JsonMinifier(new JsonModifierImpl());

    private void minificationTest(String json, String expectedResponse) throws JsonProcessingException {
        String response = jsonModifier.modify(json);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void shouldDoSimpleObjects() throws JsonProcessingException {
        minificationTest("{\n  \"some_field\" : 123\n}", "{\"some_field\":123}");
        minificationTest("{\n  \"some_object\" : {\n    \"x\" : 964,\n    \"y\" : \"aaaxx\"\n  }\n}",
                "{\"some_object\":{\"x\":964,\"y\":\"aaaxx\"}}");
    }

    @Test
    public void shouldMinifyArrays() throws JsonProcessingException {
        minificationTest("{\n  \"probably_array\" : [ 1, 2, 3, 4 ]\n}",
                "{\"probably_array\":[1,2,3,4]}");
        minificationTest("{\n  \"some_nested_array\" : [ 1, {\n    \"inner_array\" : [ \"abc\", \"def\" ]\n  }, 3, 4 ]\n}",
                "{\"some_nested_array\":[1,{\"inner_array\":[\"abc\",\"def\"]},3,4]}");
    }

    @Test
    public void shouldMinifyEmptyJson() throws JsonProcessingException {
        minificationTest("{ }", "{}");
        minificationTest("{              }", "{}");
    }

    @Test
    public void shouldReadPassedJson() throws JsonProcessingException
    {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        JsonNode jsonNodeMock = mock(JsonNode.class);

        when(objectMapperMock.readTree(anyString())).thenReturn(jsonNodeMock);

        JsonMinifier jsonMinifier = new JsonMinifier(new JsonModifierImpl(), objectMapperMock);
        jsonMinifier.modify("{\n  \"some_field\" : 123\n}");

        verify(objectMapperMock, times(1)).readTree("{\n  \"some_field\" : 123\n}");
    }

    @Test
    public void shouldUseMinificationOnModifier() throws JsonProcessingException
    {
        JsonModifier jsonModifierMock = mock(JsonModifier.class);

        JsonMinifier jsonMinifier = new JsonMinifier(jsonModifierMock);
        jsonMinifier.modify("{\n  \"some_field\" : 123\n}");

        verify(jsonModifierMock, times(1)).modify("{\n  \"some_field\" : 123\n}");
    }
}
