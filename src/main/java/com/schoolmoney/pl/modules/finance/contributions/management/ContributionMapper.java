package com.schoolmoney.pl.modules.finance.contributions.management;

import com.schoolmoney.pl.core.student.management.StudentMapper;
import com.schoolmoney.pl.core.user.management.UserMapper;
import com.schoolmoney.pl.modules.finance.collections.management.CollectionMapper;
import com.schoolmoney.pl.modules.finance.contributions.models.Contribution;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionDAO;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionId;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountMapper;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        uses = {
                CollectionMapper.class,
                StudentMapper.class,
                UserMapper.class,
                FinanceAccountMapper.class
        }
)
public interface ContributionMapper {
    @Mapping(target = "id", expression = "java(toMap.getContributionId().getId())")
    ContributionDAO mapToEntity(
            Contribution toMap,
            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext
    );

    @Mapping(target = "contributionId", source = "toMap", qualifiedByName = "longToObject")
    Contribution mapToDomain(
            ContributionDAO toMap,
            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext
    );

    @Named("longToObject")
    default ContributionId fromLongToObject(ContributionDAO contributionDAO) {
        return ContributionId.of(contributionDAO.getId());
    }
}
