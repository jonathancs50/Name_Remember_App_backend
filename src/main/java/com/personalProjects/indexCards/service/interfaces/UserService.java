package com.personalProjects.indexCards.service.interfaces;

import com.personalProjects.indexCards.domain.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {
    public User getCurrentUser();
    void deleteCurrentUser();
    User initializeOrUpdateUser(Jwt jwt);
}
