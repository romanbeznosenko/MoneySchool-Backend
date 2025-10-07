package com.schoolmoney.pl.core.student.management;

import com.schoolmoney.pl.core.student.models.Student;
import com.schoolmoney.pl.core.student.models.StudentDAO;
import com.schoolmoney.pl.core.student.models.StudentId;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {UserMapper.class})
public interface StudentMapper {

    @Mapping(target = "id", expression = "java(toMap.getStudentId().getId())")
    StudentDAO mapToEntity(Student toMap,
                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "studentId", source = "toMap", qualifiedByName = "longToObject")
    Student mapToDomain(StudentDAO toMap,
                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default StudentId fromLongToObject(StudentDAO studentDAO) {
        return StudentId.of(studentDAO.getId());
    }


}