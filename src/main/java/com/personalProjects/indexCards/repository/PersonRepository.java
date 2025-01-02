package com.personalProjects.indexCards.repository;

import com.personalProjects.indexCards.domain.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByUserId(String userId);
    Optional<Person> findByIdAndUserId(Long id, String userId);
    boolean existsByIdAndUserId(Long id, String userId);

}
