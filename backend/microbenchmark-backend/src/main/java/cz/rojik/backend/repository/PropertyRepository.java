package cz.rojik.backend.repository;

import cz.rojik.backend.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {

    Optional<PropertyEntity> findFirstByKey(String key);
}
