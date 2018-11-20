package cz.rojik.backend.entity;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@SQLDelete(sql = "UPDATE mbmark_user SET archived = true WHERE id = ?")
@Table(name = "mbmark_user", indexes = {
        @Index(columnList = "email", unique = true) })
public class UserEntity extends BaseEntity {

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mbmark_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
    List<BenchmarkEntity> results;

    public UserEntity() {

    }

    public UserEntity(String firstName, String lastName, String email, String password) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = password;
        this.enabled = false;
        this.created = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password=***" +
                ", created=" + created +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserEntity user = (UserEntity) o;

        if (enabled != user.enabled) return false;
        if (firstname != null ? !firstname.equals(user.firstname) : user.firstname != null) return false;
        if (lastname != null ? !lastname.equals(user.lastname) : user.lastname != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return !(created != null ? !created.equals(user.created) : user.created != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserEntity setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public UserEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserEntity setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserEntity setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public List<BenchmarkEntity> getResults() {
        return results;
    }

    public UserEntity setResults(List<BenchmarkEntity> results) {
        this.results = results;
        return this;
    }
}