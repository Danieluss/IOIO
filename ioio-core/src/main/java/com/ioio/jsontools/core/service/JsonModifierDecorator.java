package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonModifierDecorator implements JsonModifier {

    private JsonModifier jsonModifier;
    protected ObjectMapper objectMapper;

    public JsonModifierDecorator(JsonModifier jsonModifier) {
        this.jsonModifier = jsonModifier;
        objectMapper = new ObjectMapper();
    }

    @Override
    public String modify(String json) throws JsonProcessingException {
        return jsonModifier.modify(json);
    }
}
