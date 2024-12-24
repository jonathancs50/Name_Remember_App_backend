package com.personalProjects.indexCards.service.interfaces;

import com.personalProjects.indexCards.domain.entity.Person;
import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.dto.request.PersonRequestDTO;
import com.personalProjects.indexCards.dto.response.PersonResponseDTO;

import java.util.List;

public interface PersonService {

    PersonResponseDTO createPerson(PersonRequestDTO personDTO);
    PersonResponseDTO  getPersonById(long id);
    List<PersonResponseDTO> getAllPersons();
    PersonResponseDTO updatePerson(Long id, PersonRequestDTO personDTO);
    void deletePerson(Long id);

}
