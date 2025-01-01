package com.personalProjects.indexCards.controller;

import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;


@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Value("${AWS_COGNITO_USERPOOL_ID}")
    private String userPoolIdString;

    private final UserService userService;
    private final JwtDecoder jwtDecoder;
    private final CognitoIdentityProviderClient cognitoClient;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserData(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (!authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid Authorization header format"));
            }

            // Extract the token from the header
            String idToken = authorizationHeader.substring(7);
            log.info(idToken);
            // Decode and verify the token
            Jwt jwt;
            try {
                jwt = jwtDecoder.decode(idToken);
            } catch (JwtException e) {
                log.error("Invalid ID token", e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid ID token"));
            }

            // Initialize or update user in database
            User user = userService.initializeOrUpdateUser(jwt);

            // Create response object
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("givenName", user.getGivenName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving user data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (!authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid Authorization header format"));
            }

            // Extract the token from the header
            String accessToken = authorizationHeader.substring(7);
            log.info("Access Token: {}", accessToken);

            // Retrieve username from Cognito using the access token
            GetUserRequest getUserRequest = GetUserRequest.builder()
                    .accessToken(accessToken)
                    .build();

            GetUserResponse getUserResponse = cognitoClient.getUser(getUserRequest);
            String username = getUserResponse.username();
            log.info("Deleting user: {}", username);

            // Delete the user from Cognito
            String userPoolId = userPoolIdString; // Replace with your actual User Pool ID
            AdminDeleteUserRequest deleteUserRequest = AdminDeleteUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .build();

            cognitoClient.adminDeleteUser(deleteUserRequest);
            log.info("User {} deleted successfully", username);

            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (CognitoIdentityProviderException e) {
            log.error("Error deleting user from Cognito", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete user: " + e.awsErrorDetails().errorMessage()));
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}