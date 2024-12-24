package com.personalProjects.indexCards.service.impl;

import com.personalProjects.indexCards.domain.entity.Person;
import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.dto.request.PersonRequestDTO;
import com.personalProjects.indexCards.dto.response.PersonResponseDTO;
import com.personalProjects.indexCards.exception.ResourceNotFoundException;
import com.personalProjects.indexCards.mapper.PersonMapper;
import com.personalProjects.indexCards.repository.PersonRepository;
import com.personalProjects.indexCards.repository.UserRepository;
import com.personalProjects.indexCards.service.interfaces.PersonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final PersonMapper personMapper;


    @Override
    @Transactional
    public PersonResponseDTO createPerson(PersonRequestDTO personDTO) {
        User currentUser = User.getCurrentUser(userRepository);
       Person person=personMapper.toEntity(personDTO,currentUser);
       Person savedPerson=personRepository.save(person);
       return personMapper.toDTO(savedPerson);
    }

    @Override
    public PersonResponseDTO getPersonById(long id) {

        User currentUser = User.getCurrentUser(userRepository);
        Person person=personRepository.findByIdAndUserId(id,currentUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("Person not found with id"+id));
        return personMapper.toDTO(person);
    }
     @Override
    public List<PersonResponseDTO> getAllPersons() {
        User currentUser = User.getCurrentUser(userRepository);
        return personRepository.findAllByUserId(currentUser.getId()).stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
     }

     @Override
    @Transactional
    public PersonResponseDTO updatePerson(Long id,PersonRequestDTO personDTO) {
        User currentUser = User.getCurrentUser(userRepository);
        Person person=personRepository.findByIdAndUserId(id,currentUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("Person not found with id"+id));
        personMapper.updateEntityFromDTO(personDTO,person);
        Person updatedPerson=personRepository.save(person);
        return personMapper.toDTO(updatedPerson);
     }

     @Override
    @Transactional
    public void deletePerson(Long id) {
        User currentUser = User.getCurrentUser(userRepository);
        if(!personRepository.existsByIdAndUserId(id,currentUser.getId())){
            throw new ResourceNotFoundException("Person not found with id"+id);
        }
        personRepository.deleteById(id);
     }
}
