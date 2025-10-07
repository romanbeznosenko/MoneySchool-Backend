package com.schoolmoney.pl.modules.parentStudent.management;

import com.schoolmoney.pl.core.student.management.StudentMapper;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.modules.parentStudent.models.ParentStudent;
import com.schoolmoney.pl.modules.parentStudent.models.ParentStudentDAO;
import com.schoolmoney.pl.modules.parentStudent.models.ParentStudentId;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {UserMapper.class, StudentMapper.class})
public interface ParentStudentMapper {
    @Mapping(target = "id", expression = "java(toMap.getParentStudentId().getId())")
    ParentStudentDAO mapToEntity(ParentStudent toMap,
                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "parentStudentId", source = "toMap", qualifiedByName = "longToObject")
    ParentStudent mapToDomain(ParentStudentDAO toMap,
                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default ParentStudentId fromLongToObject(ParentStudentDAO parentStudentDAO) {
        return ParentStudentId.of(parentStudentDAO.getId());
    }
}