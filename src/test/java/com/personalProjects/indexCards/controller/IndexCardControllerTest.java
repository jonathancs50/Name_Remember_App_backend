package com.personalProjects.indexCards.controller;

import com.personalProjects.indexCards.domain.entity.EventType;
import com.personalProjects.indexCards.dto.request.IndexCardRequestDTO;
import com.personalProjects.indexCards.dto.response.EventResponseDTO;
import com.personalProjects.indexCards.dto.response.IndexCardResponseDTO;
import com.personalProjects.indexCards.dto.response.PersonResponseDTO;
import com.personalProjects.indexCards.service.interfaces.IndexCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexCardControllerTest {
    @Mock
    private IndexCardService indexCardService;
    @InjectMocks
    private IndexCardController indexCardController;
    private IndexCardRequestDTO indexCardRequestDTO;
    private IndexCardResponseDTO indexCardResponseDTO;
    private EventResponseDTO eventResponseDTO;
    private PersonResponseDTO personResponse;

    @BeforeEach
    void setUp() {
        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(1L);
        eventResponseDTO.setName("AWS Summit 2024");
        eventResponseDTO.setDate(LocalDate.of(2024, 12, 1));
        eventResponseDTO.setLocation("Sandton");
        eventResponseDTO.setType(EventType.CONFERENCE);
        eventResponseDTO.setDescription("Annual tech conference");

        personResponse = new PersonResponseDTO();
        personResponse.setId(1L);
        personResponse.setFirstName("Dharmesh");
        personResponse.setLastName("Shah");
        personResponse.setCompany("Hub Spot");
        personResponse.setInterests(new HashSet<>(Arrays.asList("coding", "reading")));

        indexCardRequestDTO = new IndexCardRequestDTO();
        indexCardRequestDTO.setPersonId(1L);
        indexCardRequestDTO.setEventId(1L);
//        indexCardRequestDTO.setInteractionNotes("Great conversation about AI");
//        indexCardRequestDTO.setFollowUpItems("Share article about ML");
        indexCardRequestDTO.setMemoryTriggers("Wearing red glasses");

        indexCardResponseDTO = new IndexCardResponseDTO();
        indexCardResponseDTO.setId(1L);
        indexCardResponseDTO.setPerson(personResponse);
        indexCardResponseDTO.setEvent(eventResponseDTO);
//        indexCardResponseDTO.setInteractionNotes("Great conversation about AI");
//        indexCardResponseDTO.setFollowUpItems("Share article about ML");
        indexCardResponseDTO.setMemoryTriggers("Wearing red glasses");
    }

    @Test
    void createIndexCard() {
        // Arrange
        when(indexCardService.createIndexCard(any(IndexCardRequestDTO.class))).thenReturn(indexCardResponseDTO);

        // Act
        ResponseEntity<IndexCardResponseDTO> response = indexCardController.createIndexCard(indexCardRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(indexCardResponseDTO, response.getBody());
        verify(indexCardService, times(1)).createIndexCard(any(IndexCardRequestDTO.class));
    }

    @Test
    void getIndexCardById() {
        // Arrange
        Long indexCardId = 1L;
        when(indexCardService.getIndexCardById(indexCardId)).thenReturn(indexCardResponseDTO);

        // Act
        ResponseEntity<IndexCardResponseDTO> response = indexCardController.getIndexCardById(indexCardId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(indexCardResponseDTO, response.getBody());
        verify(indexCardService, times(1)).getIndexCardById(indexCardId);
    }

    @Test
    void getAllIndexCards() {
        // Arrange
        List<IndexCardResponseDTO> indexCards = Arrays.asList(indexCardResponseDTO);
        when(indexCardService.getAllIndexCards()).thenReturn(indexCards);

        // Act
        ResponseEntity<List<IndexCardResponseDTO>> response = indexCardController.getAllIndexCards();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(indexCards, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(indexCardService, times(1)).getAllIndexCards();
    }

    @Test
    void getIndexCardsByPerson() {
        // Arrange
        Long personId = 1L;
        List<IndexCardResponseDTO> indexCards = Arrays.asList(indexCardResponseDTO);
        when(indexCardService.getIndexCardsByPersonId(personId)).thenReturn(indexCards);

        // Act
        ResponseEntity<List<IndexCardResponseDTO>> response = indexCardController.getIndexCardsByPerson(personId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(indexCards, response.getBody());
        verify(indexCardService, times(1)).getIndexCardsByPersonId(personId);
    }

    @Test
    void getIndexCardsByEvent() {
        // Arrange
        Long eventId = 1L;
        List<IndexCardResponseDTO> indexCards = Arrays.asList(indexCardResponseDTO);
        when(indexCardService.getIndexCardsByEventId(eventId)).thenReturn(indexCards);

        // Act
        ResponseEntity<List<IndexCardResponseDTO>> response = indexCardController.getIndexCardsByEvent(eventId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(indexCards, response.getBody());
        verify(indexCardService, times(1)).getIndexCardsByEventId(eventId);
    }

    @Test
    void updateIndexCard() {
        // Arrange
        Long indexCardId = 1L;
        when(indexCardService.updateIndexCard(eq(indexCardId), any(IndexCardRequestDTO.class)))
                .thenReturn(indexCardResponseDTO);

        // Act
        ResponseEntity<IndexCardResponseDTO> response =
                indexCardController.updateIndexCard(indexCardId, indexCardRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(indexCardResponseDTO, response.getBody());
        verify(indexCardService, times(1))
                .updateIndexCard(eq(indexCardId), any(IndexCardRequestDTO.class));
    }

    @Test
    void deleteIndexCard() {
        // Arrange
        Long indexCardId = 1L;
        doNothing().when(indexCardService).deleteIndexCard(indexCardId);

        // Act
        ResponseEntity<Void> response = indexCardController.deleteIndexCard(indexCardId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(indexCardService, times(1)).deleteIndexCard(indexCardId);
    }
}