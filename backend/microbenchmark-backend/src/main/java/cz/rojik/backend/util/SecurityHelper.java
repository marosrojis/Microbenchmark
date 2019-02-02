package cz.rojik.backend.util;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.enums.RoleTypeEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Component
public class SecurityHelper {

    /**
     * Get current logged user
     * @return logged user
     */
    public UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Object principal = auth.getDetails();
            if (principal instanceof UserDTO) {
                return (UserDTO) principal;
            }
        }
        return null;
    }

    /**
     * Get current logged user's ID
     * @return logged user's ID
     */
    public Long getCurrentUserId() {
        UserDTO currentUser = getCurrentUser();

        return currentUser == null ? null : currentUser.getId();
    }

    /**
     * Get curent logged user's username
     * @return logged user's username
     */
    public String getCurrentUserName() {
        UserDTO currentUser = getCurrentUser();

        return currentUser == null ? null : currentUser.getUsername();
    }

    /**
     * Check if logged user contains 'Admin' role
     * @return true if logged user contains 'Admin' role
     */
    public boolean isLoggedUserAdmin() {
        return isUserAdmin(getCurrentUser());
    }

    /**
     * Check if user contains 'Admin' role
     * @param user user with roles
     * @return true if user contains 'Admin' role
     */
    public boolean isUserAdmin(UserDTO user) {
        return user != null && user.getRoles().stream().anyMatch(r -> r.getType().equals(RoleTypeEnum.ADMIN.getRoleType()));
    }

    /**
     * Check if looged user has ID equals to ID in parameter.
     * @param id user ID
     * @return true if logged user has ID equals to ID in parameter.
     */
    public boolean hasLoggedUserId(Long id) {
        UserDTO currentUser = getCurrentUser();

        return currentUser != null && currentUser.getId().equals(id);
    }
}
