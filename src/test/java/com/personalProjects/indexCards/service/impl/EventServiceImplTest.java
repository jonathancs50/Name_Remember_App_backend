package com.personalProjects.indexCards.service.impl;

import com.personalProjects.indexCards.domain.entity.Event;
import com.personalProjects.indexCards.domain.entity.EventType;
import com.personalProjects.indexCards.dto.request.EventRequestDTO;
import com.personalProjects.indexCards.dto.response.EventResponseDTO;
import com.personalProjects.indexCards.exception.ResourceNotFoundException;
import com.personalProjects.indexCards.mapper.EventMapper;
import com.personalProjects.indexCards.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private EventRequestDTO requestDTO;
    private EventResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        event = new Event();
        event.setId(1L);
        event.setName("AWS Summit 2024");
        event.setDate(LocalDate.now());
        event.setLocation("Sandton");
        event.setType(EventType.CONFERENCE);
        event.setDescription("Annual tech conference");

        requestDTO = new EventRequestDTO();
        requestDTO.setName("AWS Summit 2024");
        requestDTO.setDate(LocalDate.now());
        requestDTO.setLocation("Sandton");
        requestDTO.setType(EventType.CONFERENCE);
        requestDTO.setDescription("Annual tech conference");

        responseDTO = new EventResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("AWS Summit 2024");
        responseDTO.setDate(LocalDate.now());
        responseDTO.setLocation("Sandton");
        responseDTO.setType(EventType.CONFERENCE);
        responseDTO.setDescription("Annual tech conference");
    }

//    @Test
//    void createEvent() {
//        // Arrange
//        when(eventMapper.toEntity(any(EventRequestDTO.class))).thenReturn(event);
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//        when(eventMapper.toDto(any(Event.class))).thenReturn(responseDTO);
//
//        // Act
//        EventResponseDTO result = eventService.createEvent(requestDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("AWS Summit 2024", result.getName());
//        assertEquals(EventType.CONFERENCE, result.getType());
//        verify(eventRepository).save(any(Event.class));
//        verify(eventMapper).toDto(any(Event.class));
//    }

    @Test
    void getEventById() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toDto(any(Event.class))).thenReturn(responseDTO);

        // Act
        EventResponseDTO result = eventService.getEventById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("AWS Summit 2024", result.getName());
        verify(eventRepository).findById(1L);
    }

    @Test
    void getEventById_NotFound() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventService.getEventById(1L));
        verify(eventRepository).findById(1L);
        verify(eventMapper, never()).toDto(any(Event.class));
    }

    @Test
    void getAllEvents() {
        // Arrange
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDto(any(Event.class))).thenReturn(responseDTO);

        // Act
        List<EventResponseDTO> results = eventService.getAllEvents();

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("AWS Summit 2024", results.get(0).getName());
        verify(eventRepository).findAll();
    }

    @Test
    void getAllEvents_EmptyList() {
        // Arrange
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EventResponseDTO> results = eventService.getAllEvents();

        // Assert
        assertTrue(results.isEmpty());
        verify(eventRepository).findAll();
        verify(eventMapper, never()).toDto(any(Event.class));
    }

    @Test
    void updateEvent() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toDto(any(Event.class))).thenReturn(responseDTO);

        // Update request with new data
        requestDTO.setName("Updated Conference");
        requestDTO.setLocation("New York");

        // Act
        EventResponseDTO result = eventService.updateEvent(1L, requestDTO);

        // Assert
        assertNotNull(result);
        verify(eventMapper).updateEntityFromDto(eq(requestDTO), eq(event));
        verify(eventRepository).save(event);
    }

    @Test
    void updateEvent_NotFound() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> eventService.updateEvent(1L, requestDTO));
        verify(eventRepository).findById(1L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void deleteEvent() {
        // Arrange
        when(eventRepository.existsById(1L)).thenReturn(true);

        // Act
        eventService.deleteEvent(1L);

        // Assert
        verify(eventRepository).deleteById(1L);
    }
    @Test
    void deleteEvent_NotFound() {
        // Arrange
        when(eventRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> eventService.deleteEvent(1L));
        verify(eventRepository, never()).deleteById(any());
    }
}