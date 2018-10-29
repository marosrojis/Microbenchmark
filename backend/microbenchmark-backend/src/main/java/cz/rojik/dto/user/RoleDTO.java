package cz.rojik.dto.user;

import cz.rojik.dto.BaseDTO;
import cz.rojik.entity.RoleEntity;

public class RoleDTO extends BaseDTO {

    private static final long serialVersionUID = 6446218251439828650L;

    private String type;
    private boolean new_;

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

    public void setType(String type) {
        this.type = type;
    }

	public void setNew(boolean new_) {
		this.new_ = new_;
	}

	public boolean getNew() {
		return new_;
	}

}
