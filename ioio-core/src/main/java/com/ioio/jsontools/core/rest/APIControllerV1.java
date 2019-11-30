package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ioio.jsontools.core.aspect.log.LogMethodCall;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.ioio.jsontools.core.service.CoreService;

@LogMethodCall
@RestController
@RequestMapping("/api/v1/")
public class APIControllerV1 {
	private final CoreService coreService;

    public APIControllerV1(CoreService coreService) {
        this.coreService = coreService;
    }

	// FIXME: advanced exception catches? status codes? itp.

    @PostMapping(value = "/modifier/maxifier")
    public String modifierMaxifier(@RequestBody String json) throws JsonProcessingException {
        return coreService.maxifier(json);
	}

    @PostMapping(value = "/modifier/minifier")
    public String modifierMinifier(@RequestBody String json) throws JsonProcessingException {
        return coreService.minifier(json);
	}

    @PostMapping(value = "/filter/whitelist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String filterWhitelist(@RequestBody ObjectNode request) throws JsonProcessingException {
        String json = request.get("json").asText();
        String filter = request.get("filter").asText();
        return coreService.whitelist(json, filter);
	}

    @PostMapping(value = "/filter/blacklist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String filterBlacklist(@RequestBody ObjectNode request) throws JsonProcessingException {
        String json = request.get("json").asText();
        String filter = request.get("filter").asText();
        return coreService.blacklist(json, filter);
	}
}
