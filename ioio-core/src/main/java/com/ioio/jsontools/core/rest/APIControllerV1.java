package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ioio.jsontools.core.aspect.log.LogMethodCall;
import com.ioio.jsontools.core.service.bind.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.ioio.jsontools.core.service.CoreService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@LogMethodCall
@RestController
@RequestMapping("/api/v1/")
public class APIControllerV1 {
    private Map<String, AbstractBind> binds;

    public APIControllerV1() {
        this.binds = new HashMap<>();
        this.binds.put("filter/blacklist", new BlacklistBind());
        this.binds.put("filter/whitelist", new WhitelistBind());
        this.binds.put("modifier/maxifier", new MaxifierBind());
        this.binds.put("modifier/minifier", new MinifierBind());
    }

	// FIXME: advanced exception catches? status codes? itp.

    @PostMapping(value = "/modifier/maxifier")
    public String modifierMaxifier(@RequestBody String json) throws JsonProcessingException {
        PayloadType plainPayload = new PayloadType(json);
        return this.binds.get("modifier/maxifier").parse(plainPayload);
	}

    @PostMapping(value = "/modifier/minifier")
    public String modifierMinifier(@RequestBody String json) throws JsonProcessingException {
        PayloadType plainPayload = new PayloadType(json);
        return this.binds.get("modifier/minifier").parse(plainPayload);
	}

    @PostMapping(value = "/filter/whitelist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String filterWhitelist(@RequestBody ObjectNode request) throws JsonProcessingException {
        PayloadType plainPayload = new PayloadType(request);
        return this.binds.get("filter/whitelist").parse(plainPayload);
	}

    @PostMapping(value = "/filter/blacklist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String filterBlacklist(@RequestBody ObjectNode request) throws JsonProcessingException {
        PayloadType plainPayload = new PayloadType(request);
        return this.binds.get("filter/blacklist").parse(plainPayload);
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
                    json = bind.parse(plainPayload);
                }
            }
        }

        return json;
    }
}
