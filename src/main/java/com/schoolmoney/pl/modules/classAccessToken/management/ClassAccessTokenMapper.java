package com.schoolmoney.pl.modules.classAccessToken.management;

import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessToken;
import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenDAO;
import com.schoolmoney.pl.modules.classAccessToken.models.ClassAccessTokenId;
import com.schoolmoney.pl.modules.classes.management.ClassMapper;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = ClassMapper.class)
public interface ClassAccessTokenMapper {
    @Mapping(target = "id", expression = "java(toMap.getClassAccessTokenId().getId())")
    ClassAccessTokenDAO mapToEntity(ClassAccessToken toMap,
                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "classAccessTokenId", source = "toMap", qualifiedByName = "longToObject")
    ClassAccessToken mapToDomain(ClassAccessTokenDAO toMap,
                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default ClassAccessTokenId fromLongToObject(ClassAccessTokenDAO classAccessTokenDAO) {
        return ClassAccessTokenId.of(classAccessTokenDAO.getId());
    }
}