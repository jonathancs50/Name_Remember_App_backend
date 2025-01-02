package com.personalProjects.indexCards.service.interfaces;

import com.personalProjects.indexCards.domain.entity.IndexCard;
import com.personalProjects.indexCards.dto.request.IndexCardRequestDTO;
import com.personalProjects.indexCards.dto.response.IndexCardResponseDTO;

import java.util.List;

public interface IndexCardService {
    IndexCardResponseDTO createIndexCard(IndexCardRequestDTO requestDTO);
    IndexCardResponseDTO getIndexCardById(Long id);
    List<IndexCardResponseDTO> getAllIndexCards();
    List<IndexCardResponseDTO> getIndexCardsByPersonId(Long personId);
    List<IndexCardResponseDTO> getIndexCardsByEventId(Long eventId);
    IndexCardResponseDTO updateIndexCard(Long id,IndexCardRequestDTO requestDTO);
    void deleteIndexCard(Long id);
    void deleteIndexCardsByEventIdAndPersonId(Long eventId, Long personId);
    void deleteIndexCardsByEventId(Long eventId);
    void deleteIndexCardsAndPersonsByEventId(Long eventId);
}
