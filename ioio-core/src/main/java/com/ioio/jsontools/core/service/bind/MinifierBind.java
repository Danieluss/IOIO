package com.ioio.jsontools.core.service.bind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierImpl;
import com.ioio.jsontools.core.service.minification.Minifier;

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