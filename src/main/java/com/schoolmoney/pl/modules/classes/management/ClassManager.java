package com.schoolmoney.pl.modules.classes.management;

import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassManager {
    private final ClassRepository classRepository;

    public ClassDAO saveToDatabase(ClassDAO classDAO) {
        return classRepository.save(classDAO);
    }

    public Page<ClassDAO> findUserTreasurerClasses(Specification<ClassDAO> specification, Pageable pageable) {
        return classRepository.findAll(specification, pageable);
    }

    public Optional<ClassDAO> findById(UUID id) {
        return classRepository.findById(id);
    }

    public Page<ClassDAO> findByParentId(UUID parentId, Pageable pageable) {
        return classRepository.findByParentId(parentId, pageable);
    }

    public void delete(ClassDAO classDAO) {
        classRepository.delete(classDAO);
    }

    public List<UserDAO> getParentsFromClass(UUID classId) {
        return classRepository.findParentsByClassId(classId);
    }

    public Page<ClassDAO> findAll(Specification<ClassDAO> specification, Pageable pageable) {
        return classRepository.findAll(specification, pageable);
    }
}