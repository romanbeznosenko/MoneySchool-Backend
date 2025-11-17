package com.schoolmoney.pl.modules.finance.collections.management;

import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class CollectionSpecifications {

    /**
     * Base specification to filter out archived collections
     */
    private static Specification<CollectionDAO> isNotArchived() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.isFalse(root.get("isArchived")),
                        criteriaBuilder.isNull(root.get("isArchived"))
                )
        );
    }

    /**
     * Find collections by list of classes (only non-archived)
     */
    public static Specification<CollectionDAO> findByClass(List<ClassDAO> classes) {
        return ((root, query, criteriaBuilder) -> {
            if (classes == null || classes.isEmpty()) {
                return isNotArchived().toPredicate(root, query, criteriaBuilder);
            }

            return criteriaBuilder.and(
                    root.get("aClass").in(classes),
                    isNotArchived().toPredicate(root, query, criteriaBuilder)
            );
        });
    }

    /**
     * Find collections by class ID (only non-archived)
     */
    public static Specification<CollectionDAO> findByClassId(UUID classId) {
        return ((root, query, criteriaBuilder) -> {
            if (classId == null) {
                return isNotArchived().toPredicate(root, query, criteriaBuilder);
            }

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("aClass").get("id"), classId),
                    isNotArchived().toPredicate(root, query, criteriaBuilder)
            );
        });
    }

    /**
     * Find collections by title (only non-archived)
     */
    public static Specification<CollectionDAO> findByTitle(String title) {
        return ((root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return isNotArchived().toPredicate(root, query, criteriaBuilder);
            }

            return criteriaBuilder.and(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title")),
                            "%" + title.toLowerCase() + "%"
                    ),
                    isNotArchived().toPredicate(root, query, criteriaBuilder)
            );
        });
    }

    /**
     * Find all non-archived collections
     */
    public static Specification<CollectionDAO> findAll() {
        return isNotArchived();
    }

    /**
     * Find archived collections only
     */
    public static Specification<CollectionDAO> findArchived() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isArchived"))
        );
    }

    /**
     * Find archived collections by class
     */
    public static Specification<CollectionDAO> findArchivedByClass(List<ClassDAO> classes) {
        return ((root, query, criteriaBuilder) -> {
            if (classes == null || classes.isEmpty()) {
                return criteriaBuilder.isTrue(root.get("isArchived"));
            }

            return criteriaBuilder.and(
                    root.get("aClass").in(classes),
                    criteriaBuilder.isTrue(root.get("isArchived"))
            );
        });
    }
}