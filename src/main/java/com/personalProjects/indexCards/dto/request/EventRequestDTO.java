package com.personalProjects.indexCards.dto.request;

import com.personalProjects.indexCards.domain.entity.EventType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequestDTO {
    @NotBlank(message = "Event name is required")
    private String name;

    private LocalDate date;

    private String location;
    private EventType type;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

}
