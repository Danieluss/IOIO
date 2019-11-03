package com.ioio.jsontools.core.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CoreController {

    @RequestMapping("/ping")
    public String ping() {
        return "Server responded properly.";
    }

}
