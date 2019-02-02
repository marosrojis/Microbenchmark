package cz.rojik.backend.dto.user;

import cz.rojik.backend.dto.BaseDTO;
import cz.rojik.backend.entity.RoleEntity;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class RoleDTO extends BaseDTO {

    private static final long serialVersionUID = 6446218251439828650L;

    private String type;

    public RoleDTO() {}

    public RoleDTO(RoleEntity role) {
	    this.id = role.getId();
	    this.type = role.getType();
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id='" + id + '\'' +
                "type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleDTO roleDTO = (RoleDTO) o;

        return !(type != null ? !type.equals(roleDTO.type) : roleDTO.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public String getType() {
        return type;
    }

    public RoleDTO setType(String type) {
        this.type = type;
        return this;
    }

}
