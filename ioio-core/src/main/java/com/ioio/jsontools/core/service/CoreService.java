package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.rest.data.ModifierData;
import com.ioio.jsontools.core.service.filter.JsonFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Provider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class CoreService {
	@Autowired
    private JsonFilter whitelist;
	@Autowired
	private JsonFilter blacklist;
	@Autowired
    private JsonModifier minifier;
	@Autowired
	private JsonModifier maxifier;
	@Autowired
	private Provider<JsonModifier.Builder> jsonModifierBuilderProvider;

    public String ping() {
        return "Server responded properly.";
    }

	public String whitelist(String json, String filter) throws JsonProcessingException {
		return whitelist.filter(json, filter);
	}

	public String blacklist(String json, String filter) throws JsonProcessingException {
		return blacklist.filter(json, filter);
	}

	public String maxify(String json) throws JsonProcessingException {
		return maxifier.modify(json);
	}

	public String minify(String json) throws JsonProcessingException {
		return minifier.modify(json);
	}

	public String combine(String json, List<ModifierData> modifiers) throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		JsonModifier.Builder builder = jsonModifierBuilderProvider.get();
        Class<JsonModifier.Builder> builderClass = (Class<JsonModifier.Builder>) builder.getClass();
		for (ModifierData modifier : modifiers) {
			Method method = builderClass.getMethod(modifier.getType().toString().toLowerCase(),String.class);
			method.invoke(builder, modifier.getParams());
        }
		return builder.build().modify(json);
	}
}
