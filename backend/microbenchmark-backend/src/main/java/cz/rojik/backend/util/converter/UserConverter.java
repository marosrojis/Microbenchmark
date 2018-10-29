package cz.rojik.backend.util.converter;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDTO entityToDTO(UserEntity entity) {
        UserDTO user = new UserDTO(entity);
        return user;
    }
}
