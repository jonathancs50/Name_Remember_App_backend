package com.personalProjects.indexCards.controller;

import com.personalProjects.indexCards.domain.entity.User;
import com.personalProjects.indexCards.exception.ErrorResponse;
import com.personalProjects.indexCards.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtDecoder jwtDecoder;
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            User user = userService.getCurrentUser();
            // Create a simplified response DTO
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", user.getId());
            userProfile.put("email", user.getEmail());
            userProfile.put("givenName", user.getGivenName());

            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserData(Authentication authentication) {
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            // Initialize or update user in database
            User user = userService.initializeOrUpdateUser(jwt);

            // Create response object
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("givenName", user.getGivenName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUserData(Authentication authentication,
//                                                @RequestHeader(value = "X-ID-Token", required = false) String idToken) {
//        Map<String, Object> userData = new HashMap<>();
//
//        // Get access token claims
//        Jwt accessToken = (Jwt) authentication.getPrincipal();
//        userData.put("accessTokenClaims", accessToken.getClaims());
//
//        // Process ID token if present
//        if (idToken != null && !idToken.isEmpty()) {
//            try {
//                Jwt idTokenJwt = jwtDecoder.decode(idToken);
//                userData.put("idTokenClaims", idTokenJwt.getClaims());
//
//                // Initialize or update user in database
//                userService.initializeUser(idTokenJwt, accessToken);
//
//                // Add the current user from database to response
//                User currentUser = userService.getCurrentUser();
//                userData.put("user", currentUser);
//            } catch (Exception e) {
//                System.err.println("Error processing ID token: " + e.getMessage());
//            }
//        }
//
//        return ResponseEntity.ok(userData);
//    }
//
//
//    @GetMapping("/profile")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<?> getUserProfile() {
//        try {
//            log.info("Fetching user profile");
//            User user = userService.getCurrentUser();
//            log.info("Retrieved user profile: {}", user);
//            return ResponseEntity.ok(user);
//        } catch (Exception e) {
//            log.error("Error fetching user profile", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse("Error fetching user profile", e.getMessage()));
//        }
//    }


//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUserData(Authentication authentication) {
//        String username = authentication.getName();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        // Add more token details for debugging
//        Map<String, Object> userData = new HashMap<>();
//        userData.put("username", username);
//        userData.put("authorities", authorities.stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList()));
//
//        // If using JWT, we can get more claims
//        if (authentication.getPrincipal() instanceof Jwt) {
//            Jwt jwt = (Jwt) authentication.getPrincipal();
//            userData.put("tokenClaims", jwt.getClaims());
//        }
//
//        return ResponseEntity.ok(userData);
//    }

    @GetMapping("/data")
    public ResponseEntity<?> getUserSpecificData(Authentication authentication) {
        String username = authentication.getName();
        // Fetch and return user-specific data based on username
        return ResponseEntity.ok("Data for user: " + username);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.ok().build();
    }
}