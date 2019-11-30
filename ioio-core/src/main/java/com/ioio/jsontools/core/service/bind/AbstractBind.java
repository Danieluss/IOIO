package com.ioio.jsontools.core.service.bind;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.locks.ReentrantLock;

public class AbstractBind {
    AbstractBind () {}

    private ReentrantLock lock = new ReentrantLock();

    public String name() {
        return "abstract";
    }

    public void set(PayloadType payload) {

    }

    public String run() throws JsonProcessingException {
        return "";
    }

    public String parse(PayloadType payload) throws JsonProcessingException {
        this.lock.lock();
        String result;
        try {
            this.set(payload);
            result = this.run();
        } finally {
            lock.unlock();
        }
        return result;
    }
}