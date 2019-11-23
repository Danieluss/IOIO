package com.ioio.jsontools.core.service;

/**
 * Implementation of a simple json modifier
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2019-11-22
 */
public class JsonModifierImpl implements JsonModifier {
    /**
     * This method does not change input json.
     * @param json string to parse
     * @return
     */
    public String modify(String json) {
        return json;
    }
}
