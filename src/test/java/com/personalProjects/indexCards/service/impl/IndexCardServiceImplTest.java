package com.personalProjects.indexCards.service.impl;

import com.personalProjects.indexCards.domain.entity.Event;
import com.personalProjects.indexCards.domain.entity.IndexCard;
import com.personalProjects.indexCards.domain.entity.Person;
import com.personalProjects.indexCards.dto.request.IndexCardRequestDTO;
import com.personalProjects.indexCards.dto.response.IndexCardResponseDTO;
import com.personalProjects.indexCards.exception.ResourceNotFoundException;
import com.personalProjects.indexCards.mapper.IndexCardMapper;
import com.personalProjects.indexCards.repository.EventRepository;
import com.personalProjects.indexCards.repository.IndexCardRepository;
import com.personalProjects.indexCards.repository.PersonRepository;
import com.personalProjects.indexCards.service.interfaces.IndexCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexCardServiceImplTest {
    @Mock
    private IndexCardRepository indexCardRepository;
    @Mock
    private IndexCardMapper indexCardMapper;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private IndexCardServiceImpl indexCardService;

    private Person person;
    private Event event;
    private IndexCard indexCard;
    private IndexCardRequestDTO requestDTO;
    private IndexCardResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);

        event = new Event();
        event.setId(1L);

        indexCard = new IndexCard();
        indexCard.setId(1L);
        indexCard.setPerson(person);
        indexCard.setEvent(event);

        requestDTO = new IndexCardRequestDTO();
        requestDTO.setPersonId(1L);
        requestDTO.setEventId(1L);

        responseDTO = new IndexCardResponseDTO();
        responseDTO.setId(1L);
    }

//    @Test
//    void createIndexCard() {
//        // Arrange
//        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
//        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
//        when(indexCardMapper.toEntity(any(), any(), any())).thenReturn(indexCard);
//        when(indexCardRepository.save(any())).thenReturn(indexCard);
//        when(indexCardMapper.toDto(any())).thenReturn(responseDTO);
//
//        // Act
//        IndexCardResponseDTO result = indexCardService.createIndexCard(requestDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        verify(indexCardRepository).save(any(IndexCard.class));
//    }

    @Test
    void createIndexCard_PersonNotFound() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> indexCardService.createIndexCard(requestDTO));
        verify(indexCardRepository, never()).save(any());
    }

//    @Test
//    void createIndexCard_WithoutEvent() {
//        // Arrange
//        requestDTO.setEventId(null);
//        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
//        when(indexCardMapper.toEntity(any(), any(), eq(null))).thenReturn(indexCard);
//        when(indexCardRepository.save(any())).thenReturn(indexCard);
//        when(indexCardMapper.toDto(any())).thenReturn(responseDTO);
//
//        // Act
//        IndexCardResponseDTO result = indexCardService.createIndexCard(requestDTO);
//
//        // Assert
//        assertNotNull(result);
//        verify(eventRepository, never()).findById(any());
//    }

    @Test
    void getIndexCardById() {
        // Arrange
        when(indexCardRepository.findById(1L)).thenReturn(Optional.of(indexCard));
        when(indexCardMapper.toDto(indexCard)).thenReturn(responseDTO);

        // Act
        IndexCardResponseDTO result = indexCardService.getIndexCardById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getIndexCardById_NotFound() {
        // Arrange
        when(indexCardRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> indexCardService.getIndexCardById(1L));
    }

    @Test
    void getAllIndexCards() {
        // Arrange
        List<IndexCard> indexCards = Arrays.asList(indexCard);
        when(indexCardRepository.findAll()).thenReturn(indexCards);
        when(indexCardMapper.toDto(any())).thenReturn(responseDTO);

        // Act
        List<IndexCardResponseDTO> results = indexCardService.getAllIndexCards();

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void getIndexCardsByPersonId() {
        // Arrange
        when(personRepository.existsById(1L)).thenReturn(true);
        when(indexCardRepository.findByPersonId(1L)).thenReturn(Arrays.asList(indexCard));
        when(indexCardMapper.toDto(any())).thenReturn(responseDTO);

        // Act
        List<IndexCardResponseDTO> results = indexCardService.getIndexCardsByPersonId(1L);

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void getIndexCardsByPersonId_PersonNotFound() {
        // Arrange
        when(personRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> indexCardService.getIndexCardsByPersonId(1L));
        verify(indexCardRepository, never()).findByPersonId(any());
    }

    @Test
    void getIndexCardsByEventId() {
        // Arrange
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(indexCardRepository.findByEventId(1L)).thenReturn(Arrays.asList(indexCard));
        when(indexCardMapper.toDto(any())).thenReturn(responseDTO);

        // Act
        List<IndexCardResponseDTO> results = indexCardService.getIndexCardsByEventId(1L);

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void updateIndexCard() {
        // Arrange
        when(indexCardRepository.findById(1L)).thenReturn(Optional.of(indexCard));
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(indexCardRepository.save(any())).thenReturn(indexCard);
        when(indexCardMapper.toDto(any())).thenReturn(responseDTO);

        // Act
        IndexCardResponseDTO result = indexCardService.updateIndexCard(1L, requestDTO);

        // Assert
        assertNotNull(result);
        verify(indexCardMapper).updateEntityFromDto(eq(requestDTO), eq(indexCard), eq(person), eq(event));
    }

    @Test
    void deleteIndexCard() {
        // Arrange
        when(indexCardRepository.existsById(1L)).thenReturn(true);

        // Act
        indexCardService.deleteIndexCard(1L);

        // Assert
        verify(indexCardRepository).deleteById(1L);
    }

    @Test
    void deleteIndexCard_NotFound() {
        // Arrange
        when(indexCardRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> indexCardService.deleteIndexCard(1L));
        verify(indexCardRepository, never()).deleteById(any());
    }
}