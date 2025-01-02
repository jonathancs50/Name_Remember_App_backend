package com.personalProjects.indexCards.service.impl;

import com.personalProjects.indexCards.domain.entity.IndexCard;
import com.personalProjects.indexCards.domain.entity.Person;
import com.personalProjects.indexCards.domain.entity.Event;
import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.dto.request.IndexCardRequestDTO;
import com.personalProjects.indexCards.dto.request.PersonRequestDTO;
import com.personalProjects.indexCards.dto.response.IndexCardResponseDTO;
import com.personalProjects.indexCards.exception.ResourceNotFoundException;
import com.personalProjects.indexCards.mapper.IndexCardMapper;
import com.personalProjects.indexCards.repository.EventRepository;
import com.personalProjects.indexCards.repository.IndexCardRepository;
import com.personalProjects.indexCards.repository.PersonRepository;
import com.personalProjects.indexCards.repository.UserRepository;
import com.personalProjects.indexCards.service.interfaces.IndexCardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexCardServiceImpl implements IndexCardService {

    private final IndexCardRepository indexCardRepository;
    private final IndexCardMapper indexCardMapper;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;



    @Override
    public IndexCardResponseDTO createIndexCard(IndexCardRequestDTO requestDTO) {
        User currentUser = User.getCurrentUser(userRepository);
        Person person=personRepository.findByIdAndUserId(requestDTO.getPersonId(), currentUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("Person not found with id: "+requestDTO.getPersonId()));
    Event event=null;
    if(requestDTO.getEventId()!=null){
        event=eventRepository.findByIdAndUserId(requestDTO.getEventId(),currentUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("Event not found with id: "+requestDTO.getEventId()));

    }
    IndexCard indexCard=indexCardMapper.toEntity(requestDTO,person,event,currentUser);
    IndexCard savedIndexCard=indexCardRepository.save(indexCard);
    return indexCardMapper.toDto(savedIndexCard);

    }

    @Override
    public IndexCardResponseDTO getIndexCardById(Long id) {
        User currentUser = User.getCurrentUser(userRepository);

        IndexCard indexCard=indexCardRepository.findByIdAndUserId(id,currentUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("IndexCard not found with id: "+id));
        return indexCardMapper.toDto(indexCard);
    }

    @Override
    public List<IndexCardResponseDTO> getAllIndexCards() {
        User currentUser = User.getCurrentUser(userRepository);

        return indexCardRepository.findAllByUserId(currentUser.getId()).stream()
                .map(indexCardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IndexCardResponseDTO> getIndexCardsByPersonId(Long personId) {
        User currentUser = User.getCurrentUser(userRepository);

        if(!personRepository.existsByIdAndUserId(personId,currentUser.getId())){
            throw new ResourceNotFoundException("Person not found with id: "+personId);

        }
        return indexCardRepository.findByPersonIdAndUserId(personId, currentUser.getId()).stream()
                .map(indexCardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IndexCardResponseDTO> getIndexCardsByEventId(Long eventId) {
        User currentUser = User.getCurrentUser(userRepository);

        if(!eventRepository.existsByIdAndUserId(eventId,currentUser.getId())){
            throw new ResourceNotFoundException("Event not found with id: "+eventId);
        }
        return indexCardRepository.findByEventIdAndUserId(eventId, currentUser.getId()).stream()
                .map(indexCardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public IndexCardResponseDTO updateIndexCard(Long id, IndexCardRequestDTO requestDTO) {
        User currentUser = User.getCurrentUser(userRepository);

        IndexCard indexCard=indexCardRepository.findByIdAndUserId(id,currentUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("IndexCard not found with id: "+id));

        //Check DB if person exists + store data in person
        Person person=personRepository.findByIdAndUserId(requestDTO.getPersonId(),currentUser.getId())
                .orElseThrow(()->new ResourceNotFoundException("Person not found with id: "+requestDTO.getPersonId()));
        Event event=null;
        //Check DB if event exists + store data in event
        if(requestDTO.getEventId()!=null){
            event=eventRepository.findByIdAndUserId(requestDTO.getEventId(),currentUser.getId())
                    .orElseThrow(()->new ResourceNotFoundException("Event not found with id: "+requestDTO.getEventId()));
        }

        indexCardMapper.updateEntityFromDto(requestDTO,indexCard,person,event);
        IndexCard updatedIndexCard=indexCardRepository.save(indexCard);
        return indexCardMapper.toDto(updatedIndexCard);
    }

    @Override
    public void deleteIndexCard(Long id) {
        User currentUser = User.getCurrentUser(userRepository);
        if(!indexCardRepository.existsByIdAndUserId(id,currentUser.getId())){
            throw new ResourceNotFoundException("IndexCard not found with id: "+id);
        }
        indexCardRepository.deleteById(id);
    }

    @Override
    public void deleteIndexCardsByEventIdAndPersonId(Long eventId, Long personId) {
        User currentUser = User.getCurrentUser(userRepository);

        // Verify that the event exists for the current user
        if (eventId != null && !eventRepository.existsByIdAndUserId(eventId, currentUser.getId())) {
            throw new ResourceNotFoundException("Event not found with id: " + eventId);
        }

        // Verify that the person exists for the current user
        if (!personRepository.existsByIdAndUserId(personId, currentUser.getId())) {
            throw new ResourceNotFoundException("Person not found with id: " + personId);
        }

        List<IndexCard> cardsToDelete = indexCardRepository.findByEventIdAndPersonIdAndUserId(eventId, personId, currentUser.getId());
        indexCardRepository.deleteAll(cardsToDelete);
    }

    @Override
    public void deleteIndexCardsByEventId(Long eventId) {
        User currentUser = User.getCurrentUser(userRepository);

        if (!eventRepository.existsByIdAndUserId(eventId, currentUser.getId())) {
            throw new ResourceNotFoundException("Event not found with id: " + eventId);
        }

        List<IndexCard> cardsToDelete = indexCardRepository.findByEventIdAndUserId(eventId, currentUser.getId());
        indexCardRepository.deleteAll(cardsToDelete);
    }

    @Transactional
    @Override
    public void deleteIndexCardsAndPersonsByEventId(Long eventId) {
        // First, get all index cards for this event
        List<IndexCard> indexCards = indexCardRepository.findByEventId(eventId);

        // Collect all person IDs from these index cards
        Set<Long> personIds = indexCards.stream()
                .map(card -> card.getPerson().getId())
                .collect(Collectors.toSet());

        // Delete the index cards first (due to foreign key constraints)
        indexCardRepository.deleteAllByEventId(eventId);  // Changed to deleteAllByEventId

        // Then delete all the associated persons
        personRepository.deleteAllById(personIds);
    }

}
