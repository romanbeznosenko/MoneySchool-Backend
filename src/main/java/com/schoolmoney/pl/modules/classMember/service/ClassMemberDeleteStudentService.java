package com.schoolmoney.pl.modules.classMember.service;

import com.schoolmoney.pl.modules.classMember.management.ClassMemberManager;
import com.schoolmoney.pl.modules.classes.management.ClassNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassMemberDeleteStudentService {
    private final ClassMemberManager classMemberManager;

    public void deleteClassMember(UUID classMemberId){
        classMemberManager.findById(classMemberId)
                .orElseThrow(ClassNotFoundException::new);

        classMemberManager.delete(classMemberId);
    }
}
