package com.personalProjects.indexCards.controller;

import com.personalProjects.indexCards.dto.request.PersonRequestDTO;
import com.personalProjects.indexCards.dto.response.PersonResponseDTO;
import com.personalProjects.indexCards.service.interfaces.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;
    @InjectMocks
    private PersonController personController;
    private PersonResponseDTO personResponse;
    private PersonRequestDTO validPersonRequest;

    @BeforeEach
    void setUp() {
        //Intialize data before test
        validPersonRequest = new PersonRequestDTO();
        validPersonRequest.setFirstName("Dharmesh");
        validPersonRequest.setLastName("Shah");
        validPersonRequest.setCompany("Hub Spot");
        validPersonRequest.setInterests(new HashSet<>(Arrays.asList("coding", "reading")));

        personResponse = new PersonResponseDTO();
        personResponse.setId(1L);
        personResponse.setFirstName("Dharmesh");
        personResponse.setLastName("Shah");
        personResponse.setCompany("Hub Spot");
        personResponse.setInterests(new HashSet<>(Arrays.asList("coding", "reading")));
    }

    @Test
    void createPerson() {
        //Arrange or setup : we arrange it so when the service.createPerson method is called we
        //do not call it but return the PersonResponse that we created so we do not use DB data
        // but simulate it.
        when(personService.createPerson(any(PersonRequestDTO.class))).thenReturn(personResponse);
        //Act :We pass the request data to the controller method to see if it matches our expected response
        ResponseEntity<PersonResponseDTO> response = personController.createPerson(validPersonRequest);
        //Assert :Create asserts if there's no data, HTTP codes do not match, DTOs do not match,how many responses are returned
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(personResponse, response.getBody());
        verify(personService,times(1)).createPerson(validPersonRequest);

    }

    @Test
    void getPerson() {
        //Arrange or Set up
        Long id = 1L;
        when(personService.getPersonById(id)).thenReturn(personResponse);

        //Act
        ResponseEntity<PersonResponseDTO> response = personController.getPerson(id);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personResponse, response.getBody());
        verify(personService,times(1)).getPersonById(id);
    }

    @Test
    void getAllPersons() {
        //Arrange or set up
        List<PersonResponseDTO> persons = Arrays.asList(personResponse);
        when(personService.getAllPersons()).thenReturn(persons);

        //Act
        ResponseEntity<List<PersonResponseDTO>> response = personController.getAllPersons();

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(persons, response.getBody());
        assertEquals(1,response.getBody().size());
        verify(personService,times(1)).getAllPersons();

    }

    @Test
    void updatePerson() {
        // Arrange
        Long personId = 1L;
        when(personService.updatePerson(eq(personId), any(PersonRequestDTO.class)))
                .thenReturn(personResponse);

        // Act
        ResponseEntity<PersonResponseDTO> response = personController.updatePerson(personId, validPersonRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personResponse, response.getBody());
        verify(personService, times(1)).updatePerson(eq(personId), any(PersonRequestDTO.class));
    }

    @Test
    void deletePerson() {
        // Arrange
        Long personId = 1L;
        doNothing().when(personService).deletePerson(personId);

        // Act
        ResponseEntity<Void> response = personController.deletePerson(personId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(personService, times(1)).deletePerson(personId);
    }
//    @Test
//    void createPerson_WithNullFirstName_ThrowsValidationException() {
//        // Arrange
//        PersonRequestDTO invalidRequest = new PersonRequestDTO();
//        invalidRequest.setLastName("Smith");
//        invalidRequest.setCompany("Tech Corp");
//        invalidRequest.setInterests(new HashSet<>(Arrays.asList("coding", "reading")));
//        // firstName is intentionally left null
//
//        // Act & Assert
//        assertThrows(MethodArgumentNotValidException.class, () ->
//                personController.createPerson(invalidRequest));
//
//        // Verify that the service was never called with invalid data
//        verify(personService, never()).createPerson(any(PersonRequestDTO.class));
//    }
}