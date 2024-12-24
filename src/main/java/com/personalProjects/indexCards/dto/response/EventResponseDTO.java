package com.personalProjects.indexCards.dto.response;

import com.personalProjects.indexCards.domain.entity.EventType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventResponseDTO {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private EventType type;
    private String description;
}
