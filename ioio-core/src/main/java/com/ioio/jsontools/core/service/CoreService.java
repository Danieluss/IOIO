package com.ioio.jsontools.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ioio.jsontools.core.service.filter.Filter;
import com.ioio.jsontools.core.service.filter.FilterService;
import com.ioio.jsontools.core.service.JsonModifier;
import com.ioio.jsontools.core.service.JsonModifierImpl;
import com.ioio.jsontools.core.service.maxification.Maxifier;

import org.springframework.stereotype.Service;

@Service
public class CoreService {
    private FilterService filterService = new FilterService(new ObjectMapper(), null);
    private JsonModifier jsonModifierMaxifier = new Maxifier(new JsonModifierImpl());

    public String ping() {
        return "Server responded properly.";
    }

	public String whitelist(String json, String filter) throws JsonProcessingException {
		return filterService.filter(json, filter, Filter.WHITELIST);
	}

	public String blacklist(String json, String filter) throws JsonProcessingException {
		return filterService.filter(json, filter, Filter.BLACKLIST);
	}

	public String maxifier(String json) throws JsonProcessingException {
		return jsonModifierMaxifier.modify(json);
	}

	public String minifier(String json) throws JsonProcessingException {
		return "NOT IMPLEMENTED"; // FIXME: @blazej.krzyzanek
	}
}
