package cz.rojik.util;

import cz.rojik.dto.user.UserDTO;
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
}
