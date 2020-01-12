package com.ioio.jsontools.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.difflib.algorithm.DiffException;
import com.ioio.jsontools.base.aspect.log.LogMethodCall;
import com.ioio.jsontools.core.rest.data.DiffData;
import com.ioio.jsontools.core.rest.data.JsonFilterData;
import com.ioio.jsontools.core.rest.data.JsonModifiersData;
import com.ioio.jsontools.core.service.CoreService;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

import static com.ioio.jsontools.core.rest.CoreRestDescriptor.*;

@LogMethodCall
@RestController
@RequestMapping("/" + API + "/" + VERSION)
public class CoreController {
	private final CoreService coreService;

    public CoreController(CoreService coreService) {
        this.coreService = coreService;
    }

	// FIXME: advanced exception catches? status codes? itp.

    @GetMapping(value = "/ping")
    public String ping() {
        return coreService.ping();
    }

    @PostMapping(value = MAXIFY_REST)
    public String maxify(@RequestBody String json) throws JsonProcessingException {
        return coreService.maxify(json);
	}

    @PostMapping(value = MINIFY_REST)
    public String minify(@RequestBody String json) throws JsonProcessingException {
        return coreService.minify(json);
	}

    @PostMapping(value = WHITELIST_REST)
    public String whitelist(@RequestBody JsonFilterData jsonFilterData) throws JsonProcessingException {
        return coreService.whitelist(jsonFilterData.getJson(), jsonFilterData.getFilter());
	}

    @PostMapping(value = BLACKLIST_REST)
    public String blacklist(@RequestBody JsonFilterData jsonFilterData) throws JsonProcessingException {
        return coreService.blacklist(jsonFilterData.getJson(), jsonFilterData.getFilter());
	}

    @PostMapping(value = COMBINED_REST)
    public String combine(@RequestBody JsonModifiersData jsonModifiersData) throws JsonProcessingException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return coreService.combine(jsonModifiersData.getJson(), jsonModifiersData.getModifiers());
    }

    @PostMapping(value = DIFF_REST)
    public String diff(@RequestBody DiffData diffData) throws DiffException {
        return coreService.diff(diffData.getOldText(), diffData.getNewText());
    }
}
