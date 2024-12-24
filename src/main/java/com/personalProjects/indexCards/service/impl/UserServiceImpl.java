package com.personalProjects.indexCards.service.impl;

import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.repository.UserRepository;
import com.personalProjects.indexCards.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CognitoIdentityProviderClient cognitoClient;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Override
    @Transactional
    public User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                log.error("No authentication found in SecurityContext");
                throw new RuntimeException("Not authenticated");
            }

            if (!(authentication.getPrincipal() instanceof Jwt)) {
                log.error("Principal is not a JWT token");
                throw new RuntimeException("Invalid authentication type");
            }

            Jwt jwt = (Jwt) authentication.getPrincipal();

            // Try to get username from different possible claims
            String userId = jwt.getClaimAsString("cognito:username");
            if (userId == null) {
                userId = jwt.getClaimAsString("sub");
            }

            log.info("Fetching user with ID: {}", userId);

            return userRepository.findById(userId)
                    .orElseGet(() -> {
                        log.info("User not found, initializing new user");
                        User newUser = initializeOrUpdateUser(jwt);
                        return userRepository.save(newUser);
                    });
        } catch (Exception e) {
            log.error("Error getting current user", e);
            throw new RuntimeException("Error retrieving user", e);
        }
    }

    @Override
    @Transactional
    public User initializeOrUpdateUser(Jwt jwt) {
        String userId = jwt.getSubject(); // This is the 'sub' claim
        String email = jwt.getClaimAsString("email");
        String givenName = jwt.getClaimAsString("given_name");

        // Find existing user or create new one
        User user = userRepository.findById(userId)
                .orElseGet(User::new);

        // Update user information
        user.setId(userId);
        user.setEmail(email);
        user.setGivenName(givenName);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteCurrentUser() {
        User currentUser = getCurrentUser();

        try {
            AdminDeleteUserRequest deleteUserRequest = AdminDeleteUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(currentUser.getId())
                    .build();

            try {
                cognitoClient.adminDeleteUser(deleteUserRequest);
            } catch (UserNotFoundException e) {
                // If user doesn't exist in Cognito, proceed with local deletion
                log.warn("User not found in Cognito, proceeding with local deletion");
            }

            userRepository.deleteById(currentUser.getId());
            log.info("User deleted successfully: {}", currentUser.getId());

        } catch (CognitoIdentityProviderException e) {
            if (!(e instanceof UserNotFoundException)) {
                throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
            }
            // If it's UserNotFoundException, proceed with local deletion
            userRepository.deleteById(currentUser.getId());
        }
    }
}