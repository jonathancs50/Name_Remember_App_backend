package com.personalProjects.indexCards.service.impl;

import com.personalProjects.indexCards.domain.entity.Person;
import com.personalProjects.indexCards.dto.request.PersonRequestDTO;
import com.personalProjects.indexCards.dto.response.PersonResponseDTO;
import com.personalProjects.indexCards.exception.ResourceNotFoundException;
import com.personalProjects.indexCards.mapper.PersonMapper;
import com.personalProjects.indexCards.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {
@Mock
private PersonRepository personRepository;
@Mock
private PersonMapper personMapper;
@InjectMocks
private PersonServiceImpl personService;
private Person person;
private PersonRequestDTO personRequestDTO;
private PersonResponseDTO personResponseDTO;
private Set<String> interests;

@BeforeEach
void setUp() {
    interests = new HashSet<>(Arrays.asList("coding", "reading"));

    person = new Person();
    person.setId(1L);
    person.setFirstName("Dharmesh");
    person.setLastName("Shah");
    person.setCompany("Hub Spot");
    person.setInterests(interests);

    personRequestDTO = new PersonRequestDTO();
    personRequestDTO.setFirstName("Dharmesh");
    personRequestDTO.setLastName("Shah");
    personRequestDTO.setCompany("Hub Spot");
    personRequestDTO.setInterests(interests);

    personResponseDTO = new PersonResponseDTO();
    personResponseDTO.setId(1L);
    personResponseDTO.setFirstName("Dharmesh");
    personResponseDTO.setLastName("Shah");
    personResponseDTO.setCompany("Hub Spot");
    personResponseDTO.setInterests(interests);
}

//    @Test
//    void createPerson() {
//        // Arrange
//        when(personMapper.toEntity(any(PersonRequestDTO.class))).thenReturn(person);
//        when(personRepository.save(any(Person.class))).thenReturn(person);
//        when(personMapper.toDTO(any(Person.class))).thenReturn(personResponseDTO);
//
//        // Act
//        PersonResponseDTO result = personService.createPerson(personRequestDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(personResponseDTO.getId(), result.getId());
//        assertEquals(personResponseDTO.getFirstName(), result.getFirstName());
//        assertEquals(personResponseDTO.getLastName(), result.getLastName());
//        assertEquals(personResponseDTO.getCompany(), result.getCompany());
//        verify(personRepository).save(any(Person.class));
//        verify(personMapper).toDTO(any(Person.class));
//    }

    @Test
    void getPersonById() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toDTO(any(Person.class))).thenReturn(personResponseDTO);

        // Act
        PersonResponseDTO result = personService.getPersonById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dharmesh", result.getFirstName());
        verify(personRepository).findById(1L);
    
    }
    @Test
    void getPersonById_NotFound() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> personService.getPersonById(1L));
        verify(personRepository).findById(1L);
        verify(personMapper, never()).toDTO(any(Person.class));
    }

    @Test
    void getAllPersons() {
        // Arrange
        List<Person> persons = Arrays.asList(person);
        when(personRepository.findAll()).thenReturn(persons);
        when(personMapper.toDTO(any(Person.class))).thenReturn(personResponseDTO);

        // Act
        List<PersonResponseDTO> results = personService.getAllPersons();

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Dharmesh", results.get(0).getFirstName());
        verify(personRepository).findAll();
    }

    @Test
    void getAllPersons_EmptyList() {
        // Arrange
        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<PersonResponseDTO> results = personService.getAllPersons();

        // Assert
        assertTrue(results.isEmpty());
        verify(personRepository).findAll();
        verify(personMapper, never()).toDTO(any(Person.class));
    }

    @Test
    void updatePerson() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(personMapper.toDTO(any(Person.class))).thenReturn(personResponseDTO);

        // Update request with new data
        personRequestDTO.setFirstName("Mark");
        personRequestDTO.setRole("Senior Developer");

        // Act
        PersonResponseDTO result = personService.updatePerson(1L, personRequestDTO);

        // Assert
        assertNotNull(result);
        verify(personMapper).updateEntityFromDTO(eq(personRequestDTO), eq(person));
        verify(personRepository).save(person);
    }

    @Test
    void updatePerson_NotFound() {
        // Arrange
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePerson(1L, personRequestDTO));
        verify(personRepository).findById(1L);
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void deletePerson() {
        // Arrange
        when(personRepository.existsById(1L)).thenReturn(true);

        // Act
        personService.deletePerson(1L);

        // Assert
        verify(personRepository).deleteById(1L);
    }

    @Test
    void deletePerson_NotFound() {
        // Arrange
        when(personRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> personService.deletePerson(1L));
        verify(personRepository, never()).deleteById(any());
    }
}