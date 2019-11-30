package com.ioio.jsontools.core.service.bind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierImpl;
import com.ioio.jsontools.core.service.maxification.Maxifier;

public class MaxifierBind extends AbstractBind {
    private JsonModifier jsonModifierMaxifier = new Maxifier(new JsonModifierImpl());
    private String json;

    public String name() {
        return "modifier/maxifier";
    }

    public void set(PayloadType payload) {
        json = payload.json;
    }

    public String run() throws JsonProcessingException {
        return jsonModifierMaxifier.modify(json);
    }
}