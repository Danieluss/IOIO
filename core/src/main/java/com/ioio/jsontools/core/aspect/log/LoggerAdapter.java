package com.ioio.jsontools.core.aspect.log;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public class LoggerAdapter {

    private Logger logger;

    public LoggerAdapter(Logger logger) {
        this.logger = logger;
    }

    public void log(String string, Level level) {
        switch(level) {
            case INFO:
                logger.info(string);
                break;
            case WARN:
                logger.warn(string);
                break;
            case DEBUG:
                logger.debug(string);
                break;
            case ERROR:
                logger.error(string);
                break;
            case TRACE:
                logger.trace(string);
                break;
        }
    }
}
