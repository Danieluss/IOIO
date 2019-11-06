package com.ioio.jsontools.core.service.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class FilterConfig {

    private static final String CONFIG_FILE = "src/test/resources/filters/default_whitelist.json";
    private final ObjectMapper mapper;

    public FilterConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean
    public Resource configFile() {
        return new ClassPathResource(CONFIG_FILE);
    }

    @Bean
    public JsonNode whitelistRoot(Resource configFile) throws IOException {
        return mapper.readTree(configFile.getFile());
    }
}
