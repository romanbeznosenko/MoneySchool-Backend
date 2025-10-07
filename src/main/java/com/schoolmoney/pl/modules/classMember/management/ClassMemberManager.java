package com.schoolmoney.pl.modules.classMember.management;

import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassMemberManager {
    private final ClassMemberRepository classMemberRepository;

    public ClassMemberDAO saveToDatabase(ClassMemberDAO classMember) {
        return classMemberRepository.save(classMember);
    }

    public Page<ClassMemberDAO> findAll(Specification<ClassMemberDAO> spec, Pageable pageable){
        return classMemberRepository.findAll(spec, pageable);
    }

    public long count(Specification<ClassMemberDAO> specification) {
        return classMemberRepository.count(specification);
    }

    public Optional<ClassMemberDAO> findById(UUID id){
        return classMemberRepository.findById(id);
    }

    public void delete(UUID id){
        classMemberRepository.deleteById(id);
    }
}