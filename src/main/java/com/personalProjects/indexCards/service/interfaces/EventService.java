package com.personalProjects.indexCards.service.interfaces;

import com.personalProjects.indexCards.domain.entity.Event;
import com.personalProjects.indexCards.dto.request.EventRequestDTO;
import com.personalProjects.indexCards.dto.response.EventResponseDTO;

import java.util.List;

public interface EventService {

    EventResponseDTO createEvent(EventRequestDTO eventRequestDTO);
    EventResponseDTO getEventById(Long id);
    List<EventResponseDTO> getAllEvents();
    EventResponseDTO updateEvent(Long id,EventRequestDTO eventRequestDTO);
    void deleteEvent(Long id);
}
