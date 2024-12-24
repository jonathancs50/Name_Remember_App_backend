package com.personalProjects.indexCards.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class IndexCardResponseDTO {
    private Long id;
    private PersonResponseDTO person;
    private EventResponseDTO event;
//    private String interactionNotes;
//    private String followUpItems;
    private String memoryTriggers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
