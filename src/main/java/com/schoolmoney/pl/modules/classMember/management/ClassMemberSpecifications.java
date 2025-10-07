package com.schoolmoney.pl.modules.classMember.management;

import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ClassMemberSpecifications {

    public static Specification<ClassMemberDAO> findByClassId(UUID classId) {
        return (root, query, criteriaBuilder) -> {
            if (classId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("aClass").get("id"), classId);
        };
    }

    public static Specification<ClassMemberDAO> findByParentId(UUID parentId) {
        return (root, query, criteriaBuilder) -> {
            if (parentId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("student").get("parent").get("id"),
                    parentId
            );
        };
    }

    public static Specification<ClassMemberDAO> findByStudentId(UUID studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("student").get("id"), studentId);
        };
    }

    public static Specification<ClassMemberDAO> findConfirmed() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isConfirmed"), true);
    }

    public static Specification<ClassMemberDAO> findNotArchived() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isArchived"), false);
    }
}