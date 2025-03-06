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
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUsername = authentication.getName();
        return userRepository.findByUsername(currentUsername)
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }
}