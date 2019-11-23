package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Interface that is used by classes modifying json string.
 * This interface is used in decorator pattern.
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2019-11-22
 */
public interface JsonModifier {
    /**
     * This method modifies json structure
     * @param json string to parse
     * @return parsed json string
     * @throws JsonProcessingException for invalid json format
     */
    public String modify(String json) throws JsonProcessingException;
}
