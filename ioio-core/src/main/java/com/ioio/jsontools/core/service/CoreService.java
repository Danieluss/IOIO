package com.ioio.jsontools.core.service;

import org.springframework.stereotype.Service;

@Service
public class CoreService {
    public String ping() {
        return "Server responded properly.";
    }
}
