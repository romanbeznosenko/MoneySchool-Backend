package com.schoolmoney.pl.modules.classes.management;

import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ClassSpecifications {
    public static Specification<ClassDAO> findByUserTreasurer(UUID treasurerId){
        return ((root, query, criteriaBuilder) -> {
            if (treasurerId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("treasurer").get("id"), treasurerId);
        });
    }

    public static Specification<ClassDAO> findByParentId(UUID parentId) {
        return (root, query, criteriaBuilder) -> {
            if (parentId == null) {
                return criteriaBuilder.conjunction();
            }

            var subquery = query.subquery(UUID.class);
            var classMemberRoot = subquery.from(ClassMemberDAO.class);

            subquery.select(classMemberRoot.get("aClass").get("id"))
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(
                                            classMemberRoot.get("student").get("parent").get("id"),
                                            parentId
                                    ),
                                    criteriaBuilder.equal(classMemberRoot.get("isArchived"), false)
                            )
                    );

            return root.get("id").in(subquery);
        };
    }

    public static Specification<ClassDAO> hasStudentMember(UUID studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return criteriaBuilder.conjunction();
            }

            Subquery<UUID> subquery = query.subquery(UUID.class);
            var subRoot = subquery.from(ClassMemberDAO.class);

            subquery.select(subRoot.get("aClass").get("id"))
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(subRoot.get("student").get("id"), studentId),
                                    criteriaBuilder.isTrue(subRoot.get("isConfirmed")),
                                    criteriaBuilder.isFalse(subRoot.get("isArchived"))
                            )
                    );

            return criteriaBuilder.in(root.get("id")).value(subquery);
        };
    }
}
