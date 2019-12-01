package com.ioio.jsontools.core.service.filter;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
public enum FilterStrategy {
    WHITELIST(true), BLACKLIST(false);

    private boolean whitelisted;
    private JsonNode defaultRoot;

    public boolean isWhitelisted() {
        return whitelisted;
    }

    private FilterStrategy(boolean whitelisted) {
        this.whitelisted = whitelisted;
    }

    @AllArgsConstructor
    @Component
    public static class ConfigResolver {

        private JsonNode whitelistRoot;
        private JsonNode blacklistRoot;

        @PostConstruct
        public void resolveConfig() {
            FilterStrategy.WHITELIST.defaultRoot = whitelistRoot;
            FilterStrategy.BLACKLIST.defaultRoot = blacklistRoot;
        }
    }

}
