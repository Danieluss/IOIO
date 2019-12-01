package com.ioio.jsontools.core.service.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierDecorator;

/**
 * Class filtering json string according to the provided pattern
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2019-11-22
 */
public class JsonFilterModifier extends JsonModifierDecorator {

    /**
     * filter in json format
     */
    private String filterJson;
    /**
     * variable used to execute filtering functions
     */
    private final JsonFilter jsonFilter;

    /**
     * Constructor
     * @param jsonModifier object that is decorated
     * @param filterJson filter in json format
     */
    public JsonFilterModifier(JsonModifier jsonModifier, String filterJson, JsonFilter jsonFilter) {
        super(jsonModifier);
        this.filterJson = filterJson;
        this.jsonFilter = jsonFilter;
    }

    /**
     * Constructor
     * @param jsonModifier object that is decorated
     */
    public JsonFilterModifier(JsonModifier jsonModifier, JsonFilter jsonFilter) {
        super(jsonModifier);
        this.jsonFilter = jsonFilter;
    }

    /**
     * Method that filters json received from previous decorators
     * @param json string to filter
     * @return filtered json string
     * @throws JsonProcessingException for invalid json format
     */
    public String modify(String json) throws JsonProcessingException {
        if (filterJson != null) {
            return jsonFilter.filter(super.modify(json), filterJson);
        }
        return jsonFilter.filter(super.modify(json));
    }
}
