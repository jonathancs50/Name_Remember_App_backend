package com.personalProjects.indexCards.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PersonResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String pronunciation;
    private String company;
    private String role;
//    private String physicalDescription;
    private String personalNotes;
    private Set<String> interests;
    private String meetingContext;

}
