package cz.rojik.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Base repository implementing soft delete.
 * @author Marek Rojik [marek@rojik.cz]
 *
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long> {
    @Override
    @Query("select e from #{#entityName} e where e.id = :id and e.archived = ''")
    Optional<T> findById(@Param("id") Long id);

    @Override
    @Query("select case when count(e) > 0 then true else false end from #{#entityName} e where e.id = :id and e.archived = ''")
    boolean existsById(@Param("id") Long id);

    @Override
    @Query("select e from #{#entityName} e where e.archived = ''")
    List<T> findAll();

    @Override
    @Query("select count(e) from #{#entityName} e where e.archived = ''")
    long count();


}
