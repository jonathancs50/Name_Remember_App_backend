package com.personalProjects.indexCards.repository;

import com.personalProjects.indexCards.domain.entity.IndexCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndexCardRepository extends JpaRepository<IndexCard, Long> {

    List<IndexCard> findByPersonId(Long personId);
    List<IndexCard> findByEventId(Long eventId);
    List<IndexCard> findAllByUserId(String userId);
    Optional<IndexCard> findByIdAndUserId(Long id, String userId);
    boolean existsByIdAndUserId(Long id, String userId);
    List<IndexCard> findByPersonIdAndUserId(Long personId, String userId);
    List<IndexCard> findByEventIdAndUserId(Long eventId, String userId);
    List<IndexCard> findByEventIdAndPersonIdAndUserId(Long eventId, Long personId, String userId);
}
