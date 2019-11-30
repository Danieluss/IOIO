package com.ioio.jsontools.core.service.bind;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class PayloadType {
    public String json;
    public String options;

    public PayloadType(String json) {
        this.json = json;
    }

    public PayloadType(ObjectNode request) {
        if (request.has("json")) {
            this.json = request.get("json").asText();
        }
        if (request.has("filter")) {
            this.options = request.get("filter").asText();
        }
        if (request.has("payload")) {
            this.options = request.get("payload").asText();
        }
    }
}