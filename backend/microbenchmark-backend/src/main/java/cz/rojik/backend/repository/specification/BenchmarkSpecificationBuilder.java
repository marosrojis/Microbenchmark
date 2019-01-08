package cz.rojik.backend.repository.specification;

import cz.rojik.backend.entity.BenchmarkEntity;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.util.SecurityHelper;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JPA specification for get benchmarks based on optional parameters and logged user.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public class BenchmarkSpecificationBuilder {

    public static Specification<BenchmarkEntity> matchQuery(Optional<Boolean> success, Optional<Long> userId) {
        return (root, query, cb) ->{
            root.fetch("measureMethods", JoinType.INNER);
            root.fetch("user", JoinType.LEFT);
            query.distinct(true);
            query.orderBy(cb.asc(root.get("created")));

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.<Boolean>get("archived"), false));
            userId.ifPresent(user -> predicates.add(cb.equal(root.<UserEntity>get("user").<Long>get("id"), user)));
            success.ifPresent(val -> predicates.add(cb.equal(root.<Boolean>get("success"), val)));

            if (!SecurityHelper.isLoggedUserAdmin()) {
                predicates.add(cb.equal(root.<UserEntity>get("user").<Long>get("id"), SecurityHelper.getCurrentUserId()));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
