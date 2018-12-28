package cz.rojik.backend.repository;

import cz.rojik.backend.entity.BenchmarkStateEntity;
import cz.rojik.backend.enums.BenchmarkStateTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenchmarkStateRepository extends BaseRepository<BenchmarkStateEntity> {

    @Query("SELECT COUNT(b) FROM BenchmarkStateEntity b WHERE b.type IN :type AND b.archived = false")
    int countAllByStateType(@Param("type") List<BenchmarkStateTypeEnum> type);

    @Query("SELECT b FROM BenchmarkStateEntity b LEFT JOIN FETCH b.user u WHERE b.archived = false ORDER BY b.updated")
    List<BenchmarkStateEntity> findAllByOrderByUpdated();

    @Query("SELECT b FROM BenchmarkStateEntity b LEFT JOIN FETCH b.user u WHERE b.type IN :type AND b.archived = false ORDER BY b.updated")
    List<BenchmarkStateEntity> findAllByTypeIsInOrderByUpdated(@Param("type") List<BenchmarkStateTypeEnum> type);

    List<BenchmarkStateEntity> findAllByProjectIdIsNotAndTypeInAndArchivedIsFalse(String projectId, List<BenchmarkStateTypeEnum> type);

    @Query("SELECT b FROM BenchmarkStateEntity b LEFT JOIN FETCH b.user u WHERE b.projectId = :projectId AND b.archived = false")
    Optional<BenchmarkStateEntity> findByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("DELETE FROM BenchmarkStateEntity")
    void deleteAll();
}
