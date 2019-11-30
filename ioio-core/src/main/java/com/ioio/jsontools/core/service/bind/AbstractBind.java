package com.ioio.jsontools.core.service.bind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AbstractBind {
    AbstractBind () {}

    public String name() {
        return "abstract";
    }

    public void set(PayloadType payload) {

    }

    public String run() throws JsonProcessingException {
        return "";
    }
}