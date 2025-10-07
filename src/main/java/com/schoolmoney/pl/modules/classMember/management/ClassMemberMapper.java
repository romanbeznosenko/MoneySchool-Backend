package com.schoolmoney.pl.modules.classMember.management;

import com.schoolmoney.pl.modules.classMember.models.ClassMember;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberDAO;
import com.schoolmoney.pl.modules.classMember.models.ClassMemberId;
import com.schoolmoney.pl.modules.classes.management.ClassMapper;
import com.schoolmoney.pl.core.student.management.StudentMapper;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ClassMapper.class, StudentMapper.class})
public interface ClassMemberMapper {

    @Mapping(target = "id", expression = "java(toMap.getClassMemberId().getId())")
    ClassMemberDAO mapToEntity(ClassMember toMap,
                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "classMemberId", source = "toMap", qualifiedByName = "longToObject")
    ClassMember mapToDomain(ClassMemberDAO toMap,
                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default ClassMemberId fromLongToObject(ClassMemberDAO classMemberDAO) {
        return ClassMemberId.of(classMemberDAO.getId());
    }
}