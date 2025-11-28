package com.schoolmoney.pl.core.accountActivation.management;

import com.schoolmoney.pl.core.accountActivation.models.VerificationCode;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeDAO;
import com.schoolmoney.pl.core.accountActivation.models.VerificationCodeId;
import com.schoolmoney.pl.core.authAccount.management.AuthAccountMapper;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {AuthAccountMapper.class})
public interface VerificationCodeMapper {

    @Mapping(target = "id", expression = "java(toMap.getVerificationCodeId().getId())")
    VerificationCodeDAO mapToEntity(VerificationCode toMap,
                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "verificationCodeId", source = "toMap", qualifiedByName = "longToObject")
    VerificationCode mapToDomain(VerificationCodeDAO toMap,
                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default VerificationCodeId fromLongToObject(VerificationCodeDAO verificationCodeDAO) {
        return VerificationCodeId.of(verificationCodeDAO.getId());
    }
}

