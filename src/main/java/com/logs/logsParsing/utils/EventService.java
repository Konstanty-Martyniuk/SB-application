package com.logs.logsParsing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logs.logsParsing.dao.EventDao;
import com.logs.logsParsing.domain.EventCreator;
import com.logs.logsParsing.domain.EventDto;
import com.logs.logsParsing.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.logs.logsParsing.domain.EventDto.State.FINISHED;
import static com.logs.logsParsing.domain.EventDto.State.STARTED;

@Service
public class EventService {
    private static Logger logger = LoggerFactory.getLogger(EventService.class);
    private final Connection connection;
    private final EventCreator eventCreator;
    private final ObjectMapper objectMapper;

    Map<String, EventDto> mapWithStartTime = new ConcurrentHashMap<>();
    Map<String, EventDto> mapWithFinishTime = new ConcurrentHashMap<>();

    @Autowired
    public EventService(Connection connection, EventCreator eventCreator, ObjectMapper objectMapper) {
        this.connection = connection;
        this.eventCreator = eventCreator;
        this.objectMapper = objectMapper;
    }

    public void processLogFile(String path) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.lines().forEach(this::groupLogsByState);
            generateEventAndSaveToDb(mapWithStartTime.keySet());
            logger.info("Successfully finished processing file {}", path);
        } catch (FileNotFoundException e) {
            logger.error("Failed to process file");
            e.printStackTrace();
            throw e;
        }
    }

    private void groupLogsByState(String line) {
        try {
            logger.info("Converting line to EventDto");
            EventDto eventDto = objectMapper.readValue(line, EventDto.class);
            logger.info("Grouping EventDto by state");
            if (eventDto.getState().equals(STARTED)) {
                mapWithStartTime.put(eventDto.getId(), eventDto);
            } else if (FINISHED.equals(eventDto.getState())) {
                mapWithFinishTime.put(eventDto.getId(), eventDto);
            }
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert line {} to EventDto", line);
            e.printStackTrace();
        }
    }

    private void generateEventAndSaveToDb(Set<String> eventIds) {
        try (EventDao eventDao = new EventDao(connection)) {
            for (var id : eventIds) {
                EventDto start = mapWithStartTime.get(id);
                EventDto finish = mapWithFinishTime.get(id);
                if (start != null && finish != null) {
                    logger.info("Started generating event");
                    Event event = eventCreator.createEventFromEventDto(start, finish);
                    logger.info("Saving event to database");
                    eventDao.saveEventToDataBase(event);
                    logger.info("Event {} was saved to database", event);
                } else {
                    logger.error("Missing start or finish of event, event cannot be processed");
                }
            }
        }
    }


}
