package com.ioio.jsontools.core.service.maxification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierDecorator;

/**
 * Class maxifying json string
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2019-11-22
 */
public class Maxifier extends JsonModifierDecorator {

    /**
     * Constructor
     * @param jsonModifier object that is decorated
     */
    public Maxifier(JsonModifier jsonModifier) {
        super(jsonModifier);
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
