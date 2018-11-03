package cz.rojik.backend.repository;

import cz.rojik.backend.entity.BenchmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenchmarkRepository extends JpaRepository<BenchmarkEntity, Long> {
}
