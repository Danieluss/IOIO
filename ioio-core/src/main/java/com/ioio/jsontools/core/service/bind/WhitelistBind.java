package com.ioio.jsontools.core.service.bind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ioio.jsontools.core.service.filter.Filter;
import com.ioio.jsontools.core.service.filter.FilterService;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierImpl;
import com.ioio.jsontools.core.service.maxification.Maxifier;

import com.ioio.jsontools.core.service.minification.Minifier;
import org.springframework.stereotype.Service;

@Service
public class WhitelistBind extends AbstractBind {
    private FilterService filterService = new FilterService(new ObjectMapper(), null);
    private String json;
    private String filter;

    public String name() {
        return "filter/whitelist";
    }

    public void set(PayloadType payload) {
        json = payload.json;
        filter = payload.options;
    }

    public String run() throws JsonProcessingException {
        return filterService.filter(json, filter, Filter.WHITELIST);
    }
}