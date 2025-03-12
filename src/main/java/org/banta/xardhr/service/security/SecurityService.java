package org.banta.xardhr.service.security;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.repository.ExpenseRequestRepository;
import org.banta.xardhr.repository.LeaveRequestRepository;
import org.banta.xardhr.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final ExpenseRequestRepository expenseRequestRepository;

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Auth: " + authentication);
        System.out.println("User ID to check: " + userId);

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Authentication null or not authenticated");
            return false;
        }

        String currentUsername = authentication.getName();
        System.out.println("Current username: " + currentUsername);

        boolean result = userRepository.findByUsername(currentUsername)
                .map(user -> {
                    System.out.println("Found user ID: " + user.getId());
                    System.out.println("User ID class: " + user.getId().getClass().getName());
                    System.out.println("Param ID class: " + userId.getClass().getName());
                    return user.getId().equals(userId);
                })
                .orElse(false);

        System.out.println("Result: " + result);
        return result;
    }
}