package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.aspect.log.LogMethodCall;
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

    @GetMapping(value = "/test")
    public String test() {
        return "{\"lotto\":{\"lottoId\":5,\"winning-numbers\":[2,45,34,23,7,5,3],\"winners\":[{\"winnerId\":23,\"numbers\":[2,45,34,23,3,5]},{\"winnerId\":54,\"numbers\":[52,3,12,11,18,22]}]}}";
	}

	// FIXME: advanced exception catches? status codes? itp.

    @PostMapping(value = "/modifier/maxifier")
    public String modifierMaxifier(@RequestParam String json) throws JsonProcessingException {
        return coreService.maxifier(json);
	}

    @PostMapping(value = "/modifier/minifier")
    public String modifierMinifier(@RequestParam String json) throws JsonProcessingException {
        return coreService.minifier(json);
	}

    @PostMapping(value = "/filter/whitelist")
    public String filterWhitelist(@RequestParam String json, @RequestParam String filter) throws JsonProcessingException {
        return coreService.whitelist(json, filter);
	}

    @PostMapping(value = "/filter/blacklist")
    public String filterBlacklist(@RequestParam String json, @RequestParam String filter) throws JsonProcessingException {
        return coreService.blacklist(json, filter);
	}
}
