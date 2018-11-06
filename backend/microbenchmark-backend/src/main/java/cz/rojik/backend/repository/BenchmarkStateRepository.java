package cz.rojik.backend.repository;

import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenchmarkStateRepository extends JpaRepository<BenchmarkStateEntity, Long> {

    @Query("SELECT COUNT(b) FROM BenchmarkStateEntity b WHERE b.type IN :type")
    int countAllByStateType(@Param("type") List<BenchmarkStateTypeEnum> type);

    List<BenchmarkStateEntity> findAllByProjectIdIsNotAndTypeIn(String projectId, List<BenchmarkStateTypeEnum> type);

    BenchmarkStateEntity findFirstByProjectId(String projectId);
}
