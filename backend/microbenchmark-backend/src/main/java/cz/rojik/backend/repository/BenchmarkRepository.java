package cz.rojik.backend.repository;

import cz.rojik.backend.entity.BenchmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenchmarkRepository extends JpaRepository<BenchmarkEntity, Long> {

    @Query("SELECT b FROM BenchmarkEntity b JOIN FETCH b.measureMethods m JOIN FETCH b.user u ORDER BY b.created")
    List<BenchmarkEntity> findAllOrOrderByCreated();
}
