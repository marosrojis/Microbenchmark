package cz.rojik.backend.entity;

import cz.rojik.backend.enums.RoleTypeEnum;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Entity
@SQLDelete(sql = "UPDATE mbmark_role SET archived = true WHERE id = ?")
@Table(name = "mbmark_role")
public class RoleEntity extends BaseEntity {

    @Column(length=15, unique=true, nullable=false)
    private String type = RoleTypeEnum.USER.getRoleType();
    
    public RoleEntity(){
    }
    
    public RoleEntity(RoleTypeEnum role){
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

        RoleEntity role = (RoleEntity) o;

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
