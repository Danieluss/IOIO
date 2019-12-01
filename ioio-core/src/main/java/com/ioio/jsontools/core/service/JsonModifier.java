package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.service.filter.JsonFilter;
import com.ioio.jsontools.core.service.filter.JsonFilterModifier;
import com.ioio.jsontools.core.service.whitespace.JsonMaxifier;
import com.ioio.jsontools.core.service.whitespace.JsonMinifier;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Interface that is used by classes modifying json string.
 * This interface is used in decorator pattern.
 *
 * @author Kamil Piechowiak
 * @version 1.0
 * @since 2019-11-22
 */
public interface JsonModifier {
    /**
     * This method modifies json structure
     *
     * @param json string to parse
     * @return parsed json string
     * @throws JsonProcessingException for invalid json format
     */
    public String modify(String json) throws JsonProcessingException;

    @Configuration
    public static class BuilderConfig {

        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        public Builder jsonModifierBuilder(JsonFilter whitelist, JsonFilter blacklist) {
            return new Builder(new JsonModifierImpl(), whitelist, blacklist);
        }

    }

    @AllArgsConstructor
    public static class Builder {

        private JsonModifier jsonModifier = new JsonModifierImpl();
        private JsonFilter whitelist, blacklist;

        public Builder minify() {
            jsonModifier = new JsonMinifier(jsonModifier);
            return this;
        }

        public Builder maxify() {
            jsonModifier = new JsonMaxifier(jsonModifier);
            return this;
        }

        public Builder whitelist(String filter) {
            jsonModifier = new JsonFilterModifier(jsonModifier, filter, whitelist);
            return this;
        }

        public Builder blacklist(String filter) {
            jsonModifier = new JsonFilterModifier(jsonModifier, filter, blacklist);
            return this;
        }

        public JsonModifier build() {
            return jsonModifier;
        }

    }

}
