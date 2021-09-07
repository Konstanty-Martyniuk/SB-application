package com.logs.logsParsing.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
public class EventCreator {
    private static Logger logger = LoggerFactory.getLogger(EventCreator.class);
    public Event createEventFromEventDto(EventDto start, EventDto finish) {
        Long duration = finish.getTimestamp() - start.getTimestamp();
        if (duration < 0) {
            logger.error("Wrong timestamp parameters: duration cannot be negative");
            throw new InvalidParameterException("Duration cannot be negative");
        } else {
            boolean isAlert = duration > 4;

            return new Event(start.getId(), duration, start.getType(), start.getHost(), isAlert);
        }
    }
}
