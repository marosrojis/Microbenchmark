package cz.rojik.backend.repository;

import cz.rojik.backend.entity.MeasureMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureMethodRepository extends JpaRepository<MeasureMethodEntity, Long> {
}
