package com.example.demo.traits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface LogHelperTrait {
    default Logger logger() {
        return LoggerFactory.getLogger(getClass());
    }
}
