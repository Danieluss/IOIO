package com.ioio.jsontools.core.service.filter;

public enum Filter {
    WHITELIST(true), BLACKLIST(false);

    private boolean whitelisted;

    public boolean isWhitelisted() {
        return whitelisted;
    }

    private Filter(boolean whitelisted) {
        this.whitelisted = whitelisted;
    }
}
