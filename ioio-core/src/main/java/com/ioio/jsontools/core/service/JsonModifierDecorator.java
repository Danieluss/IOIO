package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Decorator for JsonModifier
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2019-11-22
 */
public class JsonModifierDecorator implements JsonModifier {

    /**
     * object that is decorated
     */
    private JsonModifier jsonModifier;
    /**
     * mapper used to convert string to JsonNode
     */
    protected ObjectMapper objectMapper;

    /**
     * Decorator's constructor
     * @param jsonModifier object that is decorated
     */
    public JsonModifierDecorator(JsonModifier jsonModifier) {
        this.jsonModifier = jsonModifier;
        objectMapper = new ObjectMapper();
    }

    /**
     * Decorator's constructor
     * @param jsonModifier object that is decorated
     * @param objectMapper object that needs to be used by decorator
     */
    public JsonModifierDecorator(JsonModifier jsonModifier, ObjectMapper objectMapper) {
        this.jsonModifier = jsonModifier;
        this.objectMapper = objectMapper;
    }

    /**
     *  This method modifies json according to jsonModifier settings.
     *  It does not add any new effects.
     * @param json string to parse
     * @return modified json string
     * @throws JsonProcessingException for invalid json format
     */
    @Override
    public String modify(String json) throws JsonProcessingException {
        return jsonModifier.modify(json);
    }
}
