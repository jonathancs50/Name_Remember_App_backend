package com.personalProjects.indexCards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class IndexCardRequestDTO {
    @NotNull(message = "Person ID is required")
    private Long personId;

    private Long eventId;

//    @Size(max = 1000, message = "Interaction notes cannot exceed 1000 characters")
//    private String interactionNotes;
//
//    @Size(max = 500, message = "Follow-up items cannot exceed 500 characters")
//    private String followUpItems;

    @Size(max = 1000, message = "Memory triggers cannot exceed 1000 characters")
    private String memoryTriggers;

}
