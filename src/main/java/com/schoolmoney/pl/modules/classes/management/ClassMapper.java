package com.schoolmoney.pl.modules.classes.management;

import com.schoolmoney.pl.modules.classes.models.ClassDAO;
import com.schoolmoney.pl.modules.classes.models.ClassId;
import com.schoolmoney.pl.modules.classes.models.Class;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {UserMapper.class})
public interface ClassMapper {

    @Mapping(target = "id", expression = "java(toMap.getClassId().getId())")
    ClassDAO mapToEntity(Class toMap,
                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "classId", source = "toMap", qualifiedByName = "longToObject")
    Class mapToDomain(ClassDAO toMap,
                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default ClassId fromLongToObject(ClassDAO classDAO) {
        return ClassId.of(classDAO.getId());
    }
}