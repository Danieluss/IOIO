package com.ioio.jsontools.core.service.whitespace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierDecorator;

/**
 * Class maxifying json string
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2019-11-22
 */
public class JsonMaxifier extends JsonModifierDecorator {

    /**
     * Constructor
     * @param jsonModifier object that is decorated
     */
    public JsonMaxifier(JsonModifier jsonModifier) {
        super(jsonModifier);
    }

    /**
     * Constructor
     * @param jsonModifier object that is decorated
     * @param objectMapper object that needs to be used by decorator
     */
    public JsonMaxifier(JsonModifier jsonModifier, ObjectMapper objectMapper) {
        super(jsonModifier, objectMapper);
    }

    /**
     * Method that changes json string received from previous decorators to a maxified form
     * @param json string to parse
     * @return maxified json in string format
     * @throws JsonProcessingException for invalid json format
     */
    public String modify(String json) throws JsonProcessingException {
        return maxify(super.modify(json));
    }

    /**
     * Method that changes json string to a maxified form
     * @param json string to parse
     * @return maxified json in string format
     * @throws JsonProcessingException for invalid json format
     */
    private String maxify(String json) throws JsonProcessingException {
        return objectMapper.readTree(json).toPrettyString();
    }
}
