package com.personalProjects.indexCards.service.impl;

import com.personalProjects.indexCards.domain.entity.Event;
import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.dto.request.EventRequestDTO;
import com.personalProjects.indexCards.dto.response.EventResponseDTO;
import com.personalProjects.indexCards.exception.ResourceNotFoundException;
import com.personalProjects.indexCards.mapper.EventMapper;
import com.personalProjects.indexCards.repository.EventRepository;
import com.personalProjects.indexCards.repository.UserRepository;
import com.personalProjects.indexCards.service.interfaces.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        User currentUser= User.getCurrentUser(userRepository);
        Event event=eventMapper.toEntity(eventRequestDTO,currentUser);
        Event savedEvent=eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        User currentUser= User.getCurrentUser(userRepository);
        Event event=eventRepository.findByIdAndUserId(id,currentUser.getId())
                .orElseThrow(()-> new RuntimeException("Event not found with id: " + id));
        return eventMapper.toDto(event);
    }

    @Override
    public List<EventResponseDTO> getAllEvents() {
        User currentUser= User.getCurrentUser(userRepository);
        return eventRepository.findAllByUserId(currentUser.getId()).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO) {
        User currentUser= User.getCurrentUser(userRepository);
       Event event=eventRepository.findByIdAndUserId(id,currentUser.getId())
               .orElseThrow(()->new ResourceNotFoundException("Event not found with id: " + id));
       eventMapper.updateEntityFromDto(eventRequestDTO, event);
       Event updatedEvent=eventRepository.save(event);
       return eventMapper.toDto(updatedEvent);

    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        User currentUser= User.getCurrentUser(userRepository);
        if(!eventRepository.existsByIdAndUserId(id,currentUser.getId())){
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
