package cz.rojik.backend.repository;

import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.entity.MeasureMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Repository
public interface MeasureMethodRepository extends BaseRepository<MeasureMethodEntity> {

    List<MeasureMethodEntity> findAllByResult(BenchmarkEntity result);
}
