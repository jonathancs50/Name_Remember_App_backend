package com.personalProjects.indexCards.repository;

import com.personalProjects.indexCards.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUserId(String userId);
    Optional<Event> findByIdAndUserId(Long id, String userId);
    boolean existsByIdAndUserId(Long id, String userId);
}
