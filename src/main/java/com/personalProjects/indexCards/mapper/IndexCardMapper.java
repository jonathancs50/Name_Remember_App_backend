package com.personalProjects.indexCards.mapper;

import com.personalProjects.indexCards.domain.entity.Event;
import com.personalProjects.indexCards.domain.entity.IndexCard;
import com.personalProjects.indexCards.domain.entity.Person;
import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.dto.request.IndexCardRequestDTO;
import com.personalProjects.indexCards.dto.response.IndexCardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexCardMapper {
    private final PersonMapper personMapper;
    private final EventMapper eventMapper;

    public IndexCard toEntity(IndexCardRequestDTO dto, Person person, Event event, User user) {
        IndexCard indexCard = new IndexCard();
        indexCard.setPerson(person);
        indexCard.setEvent(event);
//        indexCard.setInteractionNotes(dto.getInteractionNotes());
//        indexCard.setFollowUpItems(dto.getFollowUpItems());
        indexCard.setMemoryTriggers(dto.getMemoryTriggers());
        indexCard.setUser(user);
        return indexCard;
    }

    public IndexCardResponseDTO toDto(IndexCard indexCard) {
        IndexCardResponseDTO dto = new IndexCardResponseDTO();
        dto.setId(indexCard.getId());
        dto.setPerson(personMapper.toDTO(indexCard.getPerson()));
        if (indexCard.getEvent() != null) {
            dto.setEvent(eventMapper.toDto(indexCard.getEvent()));
        }
//        dto.setInteractionNotes(indexCard.getInteractionNotes());
//        dto.setFollowUpItems(indexCard.getFollowUpItems());
        dto.setMemoryTriggers(indexCard.getMemoryTriggers());
        dto.setCreatedAt(indexCard.getCreatedAt());
        dto.setUpdatedAt(indexCard.getUpdatedAt());
        return dto;
    }

    public void updateEntityFromDto(IndexCardRequestDTO dto, IndexCard indexCard, Person person, Event event){
        indexCard.setPerson(person);
        indexCard.setEvent(event);
//        indexCard.setInteractionNotes(dto.getInteractionNotes());
//        indexCard.setFollowUpItems(dto.getFollowUpItems());
        indexCard.setMemoryTriggers(dto.getMemoryTriggers());
    }

}
