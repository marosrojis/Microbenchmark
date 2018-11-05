package cz.rojik.backend.repository;

import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.entity.BenchmarkStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenchmarkStateRepository extends JpaRepository<BenchmarkStateEntity, Long> {

    @Query("SELECT COUNT(b) FROM BenchmarkStateEntity b WHERE NOT b.type = :type")
    int countAllByStateType(@Param("type") BenchmarkStateType type);

    List<BenchmarkStateEntity> findAllByProjectIdIsNotAndTypeIsNot(String projectId, BenchmarkStateType type);

    BenchmarkStateEntity findFirstByProjectId(String projectId);
}
