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

public class MinifierBind extends AbstractBind {
    private JsonModifier jsonModifierMinifier = new Minifier(new JsonModifierImpl());
    private String json;

    public String name() {
        return "modifier/minifier";
    }

    public void set(PayloadType payload) {
        json = payload.json;
    }

    public String run() throws JsonProcessingException {
        return jsonModifierMinifier.modify(json);
    }
}