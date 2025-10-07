package com.schoolmoney.pl.core.student.management;

import com.schoolmoney.pl.core.student.models.StudentDAO;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class StudentSpecifications {
    public static Specification<StudentDAO> findByParentId(UUID parentId){
        return ((root, query, criteriaBuilder) -> {
            if (parentId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("parent").get("id"), parentId);
        });
    }
}
