package com.schoolmoney.pl.modules.classAccessToken.management;

import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenDAO;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public class ClassAccessTokenSpecifications {
    public static Specification<ClassAccessTokenDAO> findActiveToken(){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("expireAt"), Instant.now()));
    }

    public static Specification<ClassAccessTokenDAO> findByClass(UUID classId){
        return (root, query, criteriaBuilder) ->{
            if (classId == null){
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("aClass").get("id"), classId);
        };
    }
}
