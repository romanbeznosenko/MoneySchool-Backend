package com.schoolmoney.pl.modules.finance.financeAccount.management;

import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccount;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountDAO;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountId;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = UserMapper.class)
public interface FinanceAccountMapper {
    @Mapping(target = "id", expression = "java(toMap.getFinanceAccountId().getId())")
    FinanceAccountDAO mapToEntity(FinanceAccount toMap,
                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "financeAccountId", source = "toMap", qualifiedByName = "longToObject")
    FinanceAccount mapToDomain(FinanceAccountDAO toMap,
                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default FinanceAccountId fromLongToObject(FinanceAccountDAO financeAccountDAO) {
        return FinanceAccountId.of(financeAccountDAO.getId());
    }
}