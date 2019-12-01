package com.ioio.jsontools.core.service.whitespace;

import com.ioio.jsontools.core.service.JsonModifierImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class WhitespaceConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JsonMaxifier maxifier() {
        return new JsonMaxifier(new JsonModifierImpl());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JsonMinifier minifier() {
        return new JsonMinifier(new JsonModifierImpl());
    }

}
