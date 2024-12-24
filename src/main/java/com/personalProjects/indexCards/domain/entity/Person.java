package com.personalProjects.indexCards.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    private String lastName;

    @Size(max = 50, message = "Pronunciation cannot exceed 50 characters")
    private String pronunciation;

    // New fields for better person identification
    private String company;
    private String role;

//    @Column(length = 1000)
//    private String physicalDescription;

    @Column(length = 1000)
    private String personalNotes;

    @ElementCollection
    @CollectionTable(name = "person_interests")
    private Set<String> interests = new HashSet<>();


    // Where/how you first met them
    @Column(length = 1000)
    private String meetingContext;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("persons")
    private User user;

}
