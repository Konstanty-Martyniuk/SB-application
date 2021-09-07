package com.logs.logsParsing.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class EventDto {
    private String id;
    private State state;
    private String type;
    private String host;
    private Long timestamp;

    @JsonCreator
    EventDto(@JsonProperty(value = "id", required = true) String id,
             @JsonProperty(value = "state", required = true) State state,
             @JsonProperty(value = "type") String type,
             @JsonProperty(value = "host") String host,
             @JsonProperty(value = "timestamp", required = true) Long timestamp) {
        this.id = id;
        this.state = state;
        this.type = type;
        this.host = host;
        this.timestamp = timestamp;
    }

    public enum State {
        @JsonProperty("STARTED")
        STARTED,
        @JsonProperty("FINISHED")
        FINISHED
    }
}


