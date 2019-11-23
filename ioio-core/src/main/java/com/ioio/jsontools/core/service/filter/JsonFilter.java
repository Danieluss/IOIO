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
public class JsonFilter extends JsonModifierDecorator {

    /**
     * filter in json format
     */
    private String filterJson;
    /**
     * informs whether it is whitelist filtering or blacklist filtering
     */
    private Filter filter;
    /**
     * variable used to execute filtering functions
     */
    private final FilterService filterService;

    /**
     * Constructor
     * @param jsonModifier object that is decorated
     * @param filterJson filter in json format
     * @param filter variable informing whether it is whitelist filtering or blacklist filtering
     */
    public JsonFilter(JsonModifier jsonModifier, String filterJson, Filter filter) {
        super(jsonModifier);
        this.filterJson = filterJson;
        this.filter = filter;
        filterService = new FilterService(objectMapper, null);
    }

    /**
     * Method that filters json received from previous decorators
     * @param json string to filter
     * @return filtered json string
     * @throws JsonProcessingException for invalid json format
     */
    public String modify(String json) throws JsonProcessingException {
        return filterService.filter(super.modify(json), filterJson, filter);
    }
}
