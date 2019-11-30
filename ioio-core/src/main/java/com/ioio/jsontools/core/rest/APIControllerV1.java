package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.ioio.jsontools.core.aspect.log.LogMethodCall;
import com.ioio.jsontools.core.service.bind.*;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.ioio.jsontools.core.service.CoreService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@LogMethodCall
@RestController
@RequestMapping("/api/v1/")
public class APIControllerV1 {
	private final CoreService coreService;
    private Map<String, AbstractBind> binds;

    public APIControllerV1(CoreService coreService) {
        this.coreService = coreService;

        this.binds.put("filter/blacklist", new BlacklistBind());
        this.binds.put("filter/whitelist", new WhitelistBind());
        this.binds.put("modifier/maxifier", new MaxifierBind());
        this.binds.put("modifier/minifier", new MinifierBind());
    }

	// FIXME: advanced exception catches? status codes? itp.

    @PostMapping(value = "/modifier/maxifier")
    public String modifierMaxifier(@RequestBody String json) throws JsonProcessingException {
        AbstractBind bind = this.binds.get("modifier/maxifier");
        PayloadType plainPayload = new PayloadType(json);
        bind.set(plainPayload);
        return bind.run();
	}

    @PostMapping(value = "/modifier/minifier")
    public String modifierMinifier(@RequestBody String json) throws JsonProcessingException {
        AbstractBind bind = this.binds.get("modifier/minifier");
        PayloadType plainPayload = new PayloadType(json);
        bind.set(plainPayload);
        return bind.run();
	}

    @PostMapping(value = "/filter/whitelist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String filterWhitelist(@RequestBody ObjectNode request) throws JsonProcessingException {
        AbstractBind bind = this.binds.get("filter/whitelist");
        PayloadType plainPayload = new PayloadType(request);
        bind.set(plainPayload);
        return bind.run();
	}

    @PostMapping(value = "/filter/blacklist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String filterBlacklist(@RequestBody ObjectNode request) throws JsonProcessingException {
        AbstractBind bind = this.binds.get("filter/blacklist");
        PayloadType plainPayload = new PayloadType(request);
        bind.set(plainPayload);
        return bind.run();
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

            for (AbstractBind bind : this.binds.values()) {
                if (type.equals(bind.name())) {
                    PayloadType plainPayload = new PayloadType(json);
                    plainPayload.options = payload;
                    bind.set(plainPayload);
                    json = bind.run();
                }
            }
        }

        return json;
    }
}
