package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioio.jsontools.core.aspect.log.LogMethodCall;
import com.ioio.jsontools.core.data.JsonFilter;
import com.ioio.jsontools.core.service.filter.Filter;
import com.ioio.jsontools.core.service.filter.FilterService;
import org.springframework.web.bind.annotation.*;

@LogMethodCall
@RestController
@RequestMapping("/api")
public class CoreController {

    private final FilterService filterService;

    public CoreController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return "Server responded properly.";
    }

    @PostMapping(value = "/whitelist/")
    public String whitelist(@RequestBody JsonFilter jsonFilter)
            throws JsonProcessingException {
        var filter = jsonFilter.getFilter();
        var json = jsonFilter.getJson();
        if (filter != null && !filter.isEmpty()) {
            return filterService.filter(json, filter, Filter.WHITELIST);
        }
        return filterService.filter(json, Filter.WHITELIST);
    }

}
