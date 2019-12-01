package com.ioio.jsontools.core.service.whitespace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierDecorator;

/**
 * Class for minification of json.
 *
 * @author blkrzyzanek
 * @version 1.0
 * @since 2019-11-24
 */
public class JsonMinifier extends JsonModifierDecorator {

    /**
     * Decorator's constructor
     *
     * @param jsonModifier object that is decorated
     */
    public JsonMinifier(JsonModifier jsonModifier) {
        super(jsonModifier);
    }

    /**
     * Method that changes json string received from previous decorators to a minified form
     *
     * @param json string to parse
     * @return maxified json in string format
     * @throws JsonProcessingException for invalid json format
     */
    public String modify(String json) throws JsonProcessingException {
        return minify(super.modify(json));
    }

    /**
     * Method that changes json string to a minified form
     *
     * @param json string to parse
     * @return maxified json in string format
     * @throws JsonProcessingException for invalid json format
     */
    private String minify(String json) throws JsonProcessingException {
        return objectMapper.readTree(json).toString();
    }
}
