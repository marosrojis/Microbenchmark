package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.RoleEntity;
import cz.rojik.backend.entity.UserEntity;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Component
public class UserConverter {

    private static Logger LOGGER = LoggerFactory.getLogger(UserConverter.class);

    /**
     * Convert {@link UserEntity} to {@link UserDTO} object
     * @param entity user entity object
     * @param setRoles if convert object must have set roles from entity.
     * @return {@link UserDTO} converted object
     */
    public UserDTO entityToDTO(UserEntity entity, boolean setRoles) {
        LOGGER.trace("Convert User entity to DTO object (setRoles = {}): {} ", setRoles, entity);

        UserDTO user = null;
        if (entity != null) {
            user = new UserDTO();
            user.setEmail(entity.getEmail())
                .setFirstname(entity.getFirstname())
                .setLastname(entity.getLastname())
                .setEnabled(entity.isEnabled())
                .setCreated(entity.getCreated())
                .setPassword(entity.getPassword());
            user.setId(entity.getId());

            if (setRoles) {
                for (RoleEntity role : entity.getRoles()) {
                    user.getRoles().add(new RoleDTO(role));
                }
            }
        }
        return user;
    }

    /**
     * Convert {@link UserDTO} to {@link UserEntity} object
     * @param user user dto object
     * @return {@link UserEntity} object
     */
    public UserEntity dtoToEntity(UserDTO user) {
        LOGGER.trace("Create entiy from develop user {}", user);
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());

        entity.setEnabled(user.isEnabled())
                .setCreated(user.getCreated())
                .setEmail(user.getEmail())
                .setFirstname(user.getFirstname())
                .setLastname(user.getLastname())
                .setPassword(user.getPassword());

        return entity;
    }

    /**
     * Map new user values to old user {@link UserEntity}
     * @param newState new user state
     * @param previousState object to update with new user values
     * @return updated user values
     */
    public UserEntity mapToEntityUpdate(UserDTO newState, UserEntity previousState) {
        LOGGER.trace("Map user dto to entity update, newState = {},\n previousState = {}", newState, previousState);
        if (!StringUtils.equals(newState.getFirstname(), previousState.getFirstname())) {
            previousState.setFirstname(newState.getFirstname());
        }
        if (!StringUtils.equals(newState.getLastname(), previousState.getLastname())) {
            previousState.setLastname(newState.getLastname());
        }
        if (newState.isEnabled() != previousState.isEnabled()) {
            previousState.setEnabled(newState.isEnabled());
        }

        return previousState;
    }
}
