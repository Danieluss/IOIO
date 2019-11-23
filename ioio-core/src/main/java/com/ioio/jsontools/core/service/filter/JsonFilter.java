package com.ioio.jsontools.core.service.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierDecorator;

public class JsonFilter extends JsonModifierDecorator {

    private String filterJson;
    private Filter filter;
    private final FilterService filterService;

    public JsonFilter(JsonModifier jsonModifier, String filterJson, Filter filter) {
        super(jsonModifier);
        this.filterJson = filterJson;
        this.filter = filter;
        filterService = new FilterService(objectMapper, null);
    }

    public String modify(String json) throws JsonProcessingException {
        return filterService.filter(json, filterJson, filter);
    }
}
