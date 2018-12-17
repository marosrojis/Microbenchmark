package cz.rojik.backend.repository;

import cz.rojik.backend.entity.PropertiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertiesRepository extends JpaRepository<PropertiesEntity, Long> {

    PropertiesEntity getFirstByKey(String key);
}
