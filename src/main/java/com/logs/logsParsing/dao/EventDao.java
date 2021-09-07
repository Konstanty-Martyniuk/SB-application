package com.logs.logsParsing.dao;

import com.logs.logsParsing.domain.Event;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class EventDao implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(EventDao.class);
    private final String QUERY = "INSERT INTO event (id, duration, type, host, alert)  VALUES (?, ?, ?, ?, ?)";
    private Connection connection;

    public EventDao(Connection connection) {
        this.connection = connection;
    }

    public void saveEventToDataBase(Event event) {

        try {
            PreparedStatement pstmt = connection.prepareStatement(QUERY);
            pstmt.setString(1, event.getId());
            pstmt.setLong(2, event.getDuration());
            pstmt.setString(3, event.getType());
            pstmt.setString(4, event.getHost());
            pstmt.setBoolean(5, event.isAlert());
            pstmt.executeUpdate();
            logger.info("Event saved to database");
        } catch (SQLException e) {
            logger.error("Failed to save event to database");
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Failed to close connection");
            e.printStackTrace();
        }
    }
}
