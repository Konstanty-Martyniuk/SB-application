package com.logs.logsParsing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class ApplicationConfiguration {
    private final String URL = "jdbc:hsqldb:mem:eventdb;ifexists=false";
    private final String DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private final String USER = "user";
    private final String PASSWORD = "";
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Bean
    public Connection connection() {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS event (" +
                    "id VARCHAR(20) NOT NULL, " +
                    "duration INTEGER NOT NULL, " +
                    "type VARCHAR(50), " +
                    "host VARCHAR(50), " +
                    "alert BOOLEAN NOT NULL)");
            return connection;
        } catch (Exception e) {
            logger.error("Failed to initialize driver");
            e.printStackTrace();
            throw new BeanCreationException("Failed to create connection to database");
        }
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
