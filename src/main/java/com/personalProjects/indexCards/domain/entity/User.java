package com.personalProjects.indexCards.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.personalProjects.indexCards.repository.UserRepository;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String id;  // This will be the Cognito username

    private String email;
    private String givenName;

    // Use synchronized sets and proper JSON annotations
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("user")
    private Set<Person> persons = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("user")
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("user")
    private Set<IndexCard> indexCards = new HashSet<>();

    // Custom getter methods to handle concurrent access
    public Set<Person> getPersons() {
        return persons != null ? new HashSet<>(persons) : new HashSet<>();
    }

    public Set<Event> getEvents() {
        return events != null ? new HashSet<>(events) : new HashSet<>();
    }

    public Set<IndexCard> getIndexCards() {
        return indexCards != null ? new HashSet<>(indexCards) : new HashSet<>();
    }

    // Helper methods to manage relationships
    public void addPerson(Person person) {
        if (persons == null) {
            persons = new HashSet<>();
        }
        persons.add(person);
        person.setUser(this);
    }

    public void addEvent(Event event) {
        if (events == null) {
            events = new HashSet<>();
        }
        events.add(event);
        event.setUser(this);
    }

    public void addIndexCard(IndexCard indexCard) {
        if (indexCards == null) {
            indexCards = new HashSet<>();
        }
        indexCards.add(indexCard);
        indexCard.setUser(this);
    }
    public static User initializeFromAuthentication(Authentication authentication) {
        User user = new User();

        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            // Set ID (Cognito username)
            String username = jwt.getClaimAsString("cognito:username");
            if (username == null) {
                username = jwt.getClaimAsString("username");
            }
            user.setId(username);

            // Set email - try multiple possible claim names
            String email = jwt.getClaimAsString("email");
            if (email == null) {
                email = jwt.getClaimAsString("cognito:email");
            }
            user.setEmail(email);

            // Set given name
            String givenName = jwt.getClaimAsString("given_name");
            if (givenName == null) {
                givenName = jwt.getClaimAsString("cognito:given_name");
            }
            user.setGivenName(givenName);
        }

        return user;
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                '}';
    }

//    public static User initializeFromAuthentication(Authentication authentication) {
//        String username = authentication.getName();
//        User user = new User();
//        user.setId(username);
//
//        if (authentication.getPrincipal() instanceof Jwt) {
//            Jwt jwt = (Jwt) authentication.getPrincipal();
//            user.setEmail(jwt.getClaimAsString("email"));
//            user.setGivenName(jwt.getClaimAsString("given_name"));
//        }
//
//        return user;
//    }

    public static User getCurrentUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findById(username)
                .orElseGet(() -> {
                    User newUser = User.initializeFromAuthentication(authentication);
                    return userRepository.save(newUser);
                });
    }
}
