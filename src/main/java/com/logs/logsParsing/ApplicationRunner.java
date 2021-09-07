package com.logs.logsParsing;

import com.logs.logsParsing.utils.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.InvalidParameterException;

@SpringBootApplication
public class ApplicationRunner implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);
    private final EventService eventService;

    @Autowired
    public ApplicationRunner(EventService eventService) {
        this.eventService = eventService;
    }

    public static void main(String[] args) {
        logger.info("Started application");
        SpringApplication.run(ApplicationRunner.class, args);
        logger.info("Application has been closed");
    }

    @Override
    public void run(String... args) throws Exception {
        if (args[0].isEmpty() || args.length != 1) {
            throw new InvalidParameterException("File path parameter cannot be empty." +
                    "\nPlease provide path to single log");
        }

        String path = args[0];
        logger.info("Trying to open file {}", path);
        eventService.processLogFile(path);
    }
}
