package com.ioio.jsontools.core.service.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class FilterConfig {

    private static final String WHITELIST_CONFIG_FILE = "filters/default_whitelist.json";
    private static final String BLACKLIST_CONFIG_FILE = "filters/default_blacklist.json";
    private final ObjectMapper mapper;

    public FilterConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean
    public JsonFilter whitelist(ObjectMapper mapper) {
        return new JsonFilter(mapper, FilterStrategy.WHITELIST);
    }

    @Bean
    public JsonFilter blacklist(ObjectMapper mapper) {
        return new JsonFilter(mapper, FilterStrategy.BLACKLIST);
    }

    @Bean
    public Resource whitelistConfigFile() {
        return new ClassPathResource(WHITELIST_CONFIG_FILE);
    }

    @Bean
    public Resource blacklistConfigFile() {
        return new ClassPathResource(BLACKLIST_CONFIG_FILE);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JsonNode whitelistRoot(Resource whitelistConfigFile) throws IOException {
        return mapper.readTree(whitelistConfigFile.getInputStream());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JsonNode blacklistRoot(Resource blacklistConfigFile) throws IOException {
        return mapper.readTree(blacklistConfigFile.getInputStream());
    }

}
