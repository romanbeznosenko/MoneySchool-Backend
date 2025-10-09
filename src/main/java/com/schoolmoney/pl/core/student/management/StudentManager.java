package com.schoolmoney.pl.core.student.management;

import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.user.models.UserDAO;
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
public class StudentManager {
    private final StudentRepository studentRepository;

    public StudentDAO saveToDatabase(StudentDAO student) {
        return studentRepository.save(student);
    }

    public Page<StudentDAO> findAll(Specification<StudentDAO> spec, Pageable pageable) {
        return studentRepository.findAll(spec, pageable);
    }

    public Optional<StudentDAO> findById(UUID id) {
        return studentRepository.findById(id);
    }

    public void deleteById(UUID id) {
        studentRepository.deleteById(id);
    }

    public List<StudentDAO> findByParentId(UUID parentId) {
        return studentRepository.findByParentIdAndIsArchivedFalse(parentId);
    }
}