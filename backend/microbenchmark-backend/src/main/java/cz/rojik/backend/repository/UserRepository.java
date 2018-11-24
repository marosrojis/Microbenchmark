package cz.rojik.backend.repository;

import cz.rojik.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {

    @Query("SELECT e FROM UserEntity e WHERE e.email = :email AND e.archived = false")
    UserEntity findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT e FROM UserEntity e JOIN FETCH e.roles RoleEntity WHERE e.archived = false")
    List<UserEntity> findAllWithRole();

    @Query("SELECT DISTINCT e FROM UserEntity e JOIN FETCH e.roles RoleEntity WHERE e.enabled = false AND e.archived = false")
    List<UserEntity> findAllNonEnabled();
}
