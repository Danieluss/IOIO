package com.ioio.jsontools.core.rest;

public class CoreRestDescriptor {

    private static final String CORE = "ioio-core";
    private static final String PING = "ping";
    public static final String API = "api";
    public static final String VERSION = "v1";

    private static final String FILTER = "filter";
    private static final String BLACKLIST = "blacklist";
    private static final String WHITELIST = "whitelist";

    private static final String MODIFIER = "modifier";
    private static final String MINIFY = "minify";
    private static final String MAXIFY = "maxify";

    private static final String COMBINED = "combined";
    public static final String COMBINED_REST = "/" + COMBINED;

    public static final String CORE_BASE = "/" + CORE + "/" + API + "/" + VERSION;

    public static final String FILTER_BASE = "/" + FILTER;
    public static final String WHITELIST_REST = FILTER_BASE + "/" + WHITELIST;
    public static final String BLACKLIST_REST = FILTER_BASE + "/" + BLACKLIST;

    public static final String MODIFIER_BASE = "/" + MODIFIER;
    public static final String MINIFY_REST = MODIFIER_BASE + "/" + MINIFY;
    public static final String MAXIFY_REST = MODIFIER_BASE + "/" + MAXIFY;

    public static final String DIFF_REST = "/diff";

    public static final String PING_REST = "/ping";

}
