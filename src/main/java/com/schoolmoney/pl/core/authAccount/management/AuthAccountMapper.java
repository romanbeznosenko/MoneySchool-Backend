package com.schoolmoney.pl.core.authAccount.management;

import com.schoolmoney.pl.core.authAccount.models.AuthAccount;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountDAO;
import com.schoolmoney.pl.core.authAccount.models.AuthAccountId;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AuthAccountMapper {
    @Mapping(target = "id", expression = "java(toMap.getAuthAccountId().getId())")
    AuthAccountDAO mapToEntity(AuthAccount toMap,
                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "authAccountId", source = "toMap", qualifiedByName = "longToObject")
    AuthAccount mapToDomain(AuthAccountDAO toMap,
                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default AuthAccountId fromLongToObject(AuthAccountDAO authAccountDAO) {
        return AuthAccountId.of(authAccountDAO.getId());
    }
}

