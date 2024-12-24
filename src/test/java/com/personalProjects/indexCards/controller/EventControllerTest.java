package com.personalProjects.indexCards.controller;

import com.personalProjects.indexCards.dto.request.EventRequestDTO;
import com.personalProjects.indexCards.dto.response.EventResponseDTO;
import com.personalProjects.indexCards.service.interfaces.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.personalProjects.indexCards.domain.entity.EventType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {
    @Mock
    private EventService eventService;
    @InjectMocks
    private EventController eventController;
    private EventRequestDTO eventRequestDTO;
    private EventResponseDTO eventResponseDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setName("AWS Summit 2024");
        eventRequestDTO.setDate(LocalDate.of(2024, 12, 1));
        eventRequestDTO.setLocation("Sandton");
        eventRequestDTO.setType(EventType.CONFERENCE);
        eventRequestDTO.setDescription("Annual tech conference");

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(1L);
        eventResponseDTO.setName("AWS Summit 2024");
        eventResponseDTO.setDate(LocalDate.of(2024, 12, 1));
        eventResponseDTO.setLocation("Sandton");
        eventResponseDTO.setType(EventType.CONFERENCE);
        eventResponseDTO.setDescription("Annual tech conference");
    }
    @Test
    void createEvent() {
        // Arrange
        when(eventService.createEvent(any(EventRequestDTO.class))).thenReturn(eventResponseDTO);

        // Act
        ResponseEntity<EventResponseDTO> response = eventController.createEvent(eventRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(eventResponseDTO, response.getBody());
        verify(eventService, times(1)).createEvent(any(EventRequestDTO.class));
    }

    @Test
    void getEventById() {
        // Arrange
        Long eventId = 1L;
        when(eventService.getEventById(eventId)).thenReturn(eventResponseDTO);

        // Act
        ResponseEntity<EventResponseDTO> response = eventController.getEventById(eventId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventResponseDTO, response.getBody());
        verify(eventService, times(1)).getEventById(eventId);
    }

    @Test
    void getAllEvents() {
        // Arrange
        List<EventResponseDTO> events = Arrays.asList(eventResponseDTO);
        when(eventService.getAllEvents()).thenReturn(events);

        // Act
        ResponseEntity<List<EventResponseDTO>> response = eventController.getAllEvents();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(events, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    void updateEvent() {
        // Arrange
        Long eventId = 1L;
        when(eventService.updateEvent(eq(eventId), any(EventRequestDTO.class)))
                .thenReturn(eventResponseDTO);

        // Act
        ResponseEntity<EventResponseDTO> response = eventController.updateEvent(eventId, eventRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventResponseDTO, response.getBody());
        verify(eventService, times(1)).updateEvent(eq(eventId), any(EventRequestDTO.class));
    }

    @Test
    void deleteEvent() {
        // Arrange
        Long eventId = 1L;
        doNothing().when(eventService).deleteEvent(eventId);

        // Act
        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService, times(1)).deleteEvent(eventId);
    }
}