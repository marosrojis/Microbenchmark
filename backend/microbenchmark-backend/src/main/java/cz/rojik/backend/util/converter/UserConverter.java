package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.RoleEntity;
import cz.rojik.backend.entity.UserEntity;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDTO entityToDTO(UserEntity entity, boolean setRoles) {
        UserDTO user = new UserDTO();
        if (entity != null) {
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

    public UserEntity dtoToEntity(UserDTO user) {
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

    public UserEntity mapToEntityUpdate(UserDTO newState, UserEntity previousState) {
        if (!StringUtils.equals(newState.getFirstname(), previousState.getFirstname())) {
            previousState.setFirstname(newState.getFirstname());
        }
        if (!StringUtils.equals(newState.getLastname(), previousState.getLastname())) {
            previousState.setLastname(newState.getLastname());
        }
        if (newState.isEnabled() != previousState.isEnabled()) {
            previousState.setEnabled(previousState.isEnabled());
        }

        return previousState;
    }
}
