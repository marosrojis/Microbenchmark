package cz.rojik.backend.repository;

import cz.rojik.backend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Repository
public interface RoleRepository extends BaseRepository<RoleEntity> {

    RoleEntity findFirstByType(String type);
}
