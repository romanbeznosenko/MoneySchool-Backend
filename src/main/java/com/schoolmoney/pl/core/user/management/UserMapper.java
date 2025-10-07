package com.schoolmoney.pl.core.user.management;

import com.schoolmoney.pl.core.user.models.User;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.core.user.models.UserId;
import com.schoolmoney.pl.core.user.models.UserResponse;
import com.schoolmoney.pl.files.storage.services.StorageService;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {})
public interface UserMapper {
    @Mapping(target = "id", expression = "java(toMap.getUserId().getId())")
    UserDAO mapToEntity(User toMap,
                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "userId", source = "toMap", qualifiedByName = "longToObject")
    User mapToDomain(UserDAO toMap,
                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "avatar", expression = "java(storageService.createPresignedGetUrl(toMap.getAvatar()))")
    UserResponse mapToResponseFromEntity(UserDAO toMap,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext,
                                         @Context StorageService storageService);

    @Mapping(target = "id", expression = "java(toMap.getUserId().getId())")
    UserResponse mapToResponse(User toMap,
                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default UserId fromLongToObject(UserDAO userDAO) {
        return UserId.of(userDAO.getId());
    }
}

