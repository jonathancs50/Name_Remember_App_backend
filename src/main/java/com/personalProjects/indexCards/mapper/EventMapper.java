package com.personalProjects.indexCards.mapper;

import com.personalProjects.indexCards.domain.entity.Event;
import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.dto.request.EventRequestDTO;
import com.personalProjects.indexCards.dto.response.EventResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public Event toEntity(EventRequestDTO dto, User user){
        Event event = new Event();
        event.setName(dto.getName());
        event.setDate(dto.getDate());
        event.setLocation(dto.getLocation());
        event.setType(dto.getType());
        event.setDescription(dto.getDescription());
        event.setUser(user);

        return event;

    }

    public EventResponseDTO toDto(Event event){
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setLocation(event.getLocation());
        dto.setType(event.getType());
        dto.setDescription(event.getDescription());
        return dto;
    }

    public void updateEntityFromDto(EventRequestDTO dto,Event event){
        event.setName(dto.getName());
        event.setDate(dto.getDate());
        event.setLocation(dto.getLocation());
        event.setType(dto.getType());
        event.setDescription(dto.getDescription());
    }
}
