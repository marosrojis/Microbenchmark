package cz.rojik.backend.repository;

import cz.rojik.backend.entity.BenchmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenchmarkRepository extends BaseRepository<BenchmarkEntity>, JpaSpecificationExecutor<BenchmarkEntity> {

    @Query("SELECT b FROM BenchmarkEntity b JOIN FETCH b.measureMethods m LEFT JOIN FETCH b.user u WHERE b.id = :id AND b.archived = false")
    Optional<BenchmarkEntity> findById(@Param("id") Long id);

    @Query("SELECT b FROM BenchmarkEntity b JOIN FETCH b.measureMethods m LEFT JOIN FETCH b.user u WHERE b.id = :id AND u.id = :userId AND b.archived = false")
    Optional<BenchmarkEntity> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

}
