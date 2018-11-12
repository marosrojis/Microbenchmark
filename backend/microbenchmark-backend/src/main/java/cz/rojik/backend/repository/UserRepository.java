package cz.rojik.backend.repository;

import cz.rojik.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    @Query("SELECT e FROM UserEntity e JOIN FETCH e.roles RoleEntity")
    List<UserEntity> findAllWithRole();
}
