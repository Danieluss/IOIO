package com.ioio.jsontools.core.aspect.log;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class LoggerAdapter {

    private Logger logger;

    public LoggerAdapter() {
        this.logger = log;
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
