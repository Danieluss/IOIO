package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ioio.jsontools.core.aspect.log.LogMethodCall;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.ioio.jsontools.core.service.CoreService;

import java.util.ArrayList;
import java.util.Iterator;

@LogMethodCall
@RestController
@RequestMapping("/api/v1/")
public class APIControllerV1 {
	private final CoreService coreService;

    public APIControllerV1(CoreService coreService) {
        this.coreService = coreService;
    }

	// FIXME: advanced exception catches? status codes? itp.
    // FIXME: filter param -> payload

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

    @PostMapping(value = "/do", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String doAll(@RequestBody ObjectNode request) throws JsonProcessingException {
        String json = request.get("json").asText();

        Iterator<JsonNode> elements = request.get("features").iterator();
        while (elements.hasNext()) {
            JsonNode element = elements.next();
            String type = element.get("type").asText();
            String payload = "";
            if (element.has("payload")) {
                payload = element.get("payload").asText();
            }

            // FIXME: iterate in registred functions in coreService

            if (type.equals("modifier/maxifier")) {
                json = coreService.maxifier(json);
            }

            if (type.equals("modifier/minifier")) {
                json = coreService.minifier(json);
            }

            if (type.equals("filter/whitelist")) {
                json = coreService.whitelist(json, payload);
            }

            if (type.equals("filter/blacklist")) {
                json = coreService.blacklist(json, payload);
            }
        }

        return json;
    }
}
