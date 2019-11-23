package com.ioio.jsontools.core.service.maxification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierDecorator;

public class Maxifier extends JsonModifierDecorator {

    public Maxifier(JsonModifier jsonModifier) {
        super(jsonModifier);
    }

    public String modify(String json) throws JsonProcessingException {
        return maxify(super.modify(json));
    }

    private String maxify(String json) throws JsonProcessingException {
        return objectMapper.readTree(json).toPrettyString();
    }
}
