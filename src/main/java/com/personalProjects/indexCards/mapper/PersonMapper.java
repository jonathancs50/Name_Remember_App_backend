package com.personalProjects.indexCards.mapper;

import com.personalProjects.indexCards.domain.entity.Person;
import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.dto.request.PersonRequestDTO;
import com.personalProjects.indexCards.dto.response.PersonResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    public Person toEntity(PersonRequestDTO dto, User user){
        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setPronunciation(dto.getPronunciation());
        person.setCompany(dto.getCompany());
        person.setRole(dto.getRole());
//        person.setPhysicalDescription(dto.getPhysicalDescription());
        person.setPersonalNotes(dto.getPersonalNotes());
        person.setInterests(dto.getInterests());
        person.setMeetingContext(dto.getMeetingContext());
        person.setUser(user);
        return person;
    }

    public PersonResponseDTO toDTO(Person person){
        PersonResponseDTO dto = new PersonResponseDTO();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setPronunciation(person.getPronunciation());
        dto.setCompany(person.getCompany());
        dto.setRole(person.getRole());
//        dto.setPhysicalDescription(person.getPhysicalDescription());
        dto.setPersonalNotes(person.getPersonalNotes());
        dto.setInterests(person.getInterests());
        dto.setMeetingContext(person.getMeetingContext());
        return dto;
    }
    public void updateEntityFromDTO(PersonRequestDTO dto, Person person){
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setPronunciation(dto.getPronunciation());
        person.setCompany(dto.getCompany());
        person.setRole(dto.getRole());
//        person.setPhysicalDescription(dto.getPhysicalDescription());
        person.setPersonalNotes(dto.getPersonalNotes());
        person.setInterests(dto.getInterests());
        person.setMeetingContext(dto.getMeetingContext());
    }
}
