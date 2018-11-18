package cz.rojik.backend.util;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.RoleType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

    public static UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Object principal = auth.getDetails();
            if (principal instanceof UserDTO) {
                return (UserDTO) principal;
            }
        }
        return null;
    }

    public static Long getCurrentUserId() {
        UserDTO currentUser = getCurrentUser();

        return currentUser == null ? null : currentUser.getId();
    }

    public static String getCurrentUserName() {
        UserDTO currentUser = getCurrentUser();

        return currentUser == null ? null : currentUser.getUsername();
    }

    public static boolean isLoggedUserAdmin() {
        UserDTO currentUser = getCurrentUser();

        return currentUser != null && currentUser.getRoles().stream().anyMatch(r -> r.getType().equals(RoleType.ADMIN.getRoleType()));
    }
}
