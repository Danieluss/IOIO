package com.ioio.jsontools.core.rest;

import com.ioio.jsontools.core.aspect.log.LogMethodCall;
import org.springframework.web.bind.annotation.*;
import com.ioio.jsontools.core.service.CoreService;

@LogMethodCall
@RestController
@RequestMapping("/api")
public class CoreController {
	private final CoreService coreService;

    public CoreController(CoreService coreService) {
        this.coreService = coreService;
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return coreService.ping();
    }
}
