package cz.rojik.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Licence uživatele
 */
@Entity
@Table(name = "mbmark_role")
public class Role extends BaseEntity {

    @Column(length=15, unique=true, nullable=false)
    private String type = RoleType.USER.getRoleType();
    
    public Role(){
    }
    
    public Role(RoleType role){
    	this.type = role.name();
    }

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Role role = (Role) o;

        if (getId() != null ? !getId().equals(role.getId()) : role.getId() != null) return false;
        return type != null ? type.equals(role.type) : role.type == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role {" +
                "id = " + getId() +
                ", type =  " + type +
                "}";
    }
}
