package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonModifier {
    public String modify(String json) throws JsonProcessingException;
}
