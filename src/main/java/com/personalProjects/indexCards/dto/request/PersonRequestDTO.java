package com.personalProjects.indexCards.dto.request;


import com.personalProjects.indexCards.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PersonRequestDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    private String lastName;

    @Size(max = 50, message = "Pronunciation cannot exceed 50 characters")
    private String pronunciation;

    private String company;
    private String role;

//    @Size(max = 1000, message = "Physical description cannot exceed 1000 characters")
//    private String physicalDescription;

    @Size(max = 1000, message = "Personal notes cannot exceed 1000 characters")
    private String personalNotes;

    private Set<String> interests;
    private String meetingContext;

}
