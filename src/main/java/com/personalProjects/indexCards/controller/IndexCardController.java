package com.personalProjects.indexCards.controller;

import com.personalProjects.indexCards.domain.entity.IndexCard;
import com.personalProjects.indexCards.dto.request.IndexCardRequestDTO;
import com.personalProjects.indexCards.dto.response.IndexCardResponseDTO;
import com.personalProjects.indexCards.service.interfaces.IndexCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/index-cards")
@RequiredArgsConstructor
public class IndexCardController {

    private final IndexCardService indexCardService;

    @PostMapping
    public ResponseEntity<IndexCardResponseDTO> createIndexCard(@Valid @RequestBody IndexCardRequestDTO indexCardDTO) {
        return new ResponseEntity<>(indexCardService.createIndexCard(indexCardDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndexCardResponseDTO> getIndexCardById(@PathVariable Long id) {
        return ResponseEntity.ok(indexCardService.getIndexCardById(id));
    }

    @GetMapping
    public ResponseEntity<List<IndexCardResponseDTO>> getAllIndexCards() {
        return ResponseEntity.ok(indexCardService.getAllIndexCards());
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<IndexCardResponseDTO>> getIndexCardsByPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(indexCardService.getIndexCardsByPersonId(personId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<IndexCardResponseDTO>> getIndexCardsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(indexCardService.getIndexCardsByEventId(eventId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndexCardResponseDTO> updateIndexCard(
            @PathVariable Long id,
            @Valid @RequestBody IndexCardRequestDTO indexCardDTO) {
        return ResponseEntity.ok(indexCardService.updateIndexCard(id, indexCardDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexCard(@PathVariable Long id) {
        indexCardService.deleteIndexCard(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/event/{eventId}/person/{personId}")
    public ResponseEntity<Void> deleteIndexCardsByEventIdAndPersonId(
            @PathVariable Long eventId,
            @PathVariable Long personId) {
        indexCardService.deleteIndexCardsByEventIdAndPersonId(eventId, personId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<Void> deleteIndexCardsByEventId(@PathVariable Long eventId) {
        indexCardService.deleteIndexCardsByEventId(eventId);
        return ResponseEntity.ok().build();
    }
}