package cz.rojik.backend.repository;

import cz.rojik.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Repository
public interface UserRepository extends BaseRepository<UserEntity> {

    @Override
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.roles r WHERE u.id = :id AND u.archived = false")
    Optional<UserEntity> findById(@Param("id") Long id);

    @Query("SELECT e FROM UserEntity e WHERE e.email = :email AND e.archived = false")
    UserEntity findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT e FROM UserEntity e JOIN FETCH e.roles r WHERE e.archived = false")
    List<UserEntity> findAllWithRole();

    @Query("SELECT DISTINCT e FROM UserEntity e JOIN FETCH e.roles r WHERE e.enabled = :enabled AND e.archived = false")
    List<UserEntity> findAllEnabled(@Param("enabled") boolean enabled);
}
