package com.schoolmoney.pl.modules.finance.collections.management;

import com.schoolmoney.pl.modules.classes.management.ClassMapper;
import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionId;
import com.schoolmoney.pl.modules.finance.financeAccount.management.FinanceAccountMapper;
import com.schoolmoney.pl.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        uses = {ClassMapper.class, FinanceAccountMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CollectionMapper {
    @Mapping(target = "id", expression = "java(toMap.getCollectionId().getId())")
    CollectionDAO mapToEntity(Collection toMap,
                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "collectionId", source = "toMap", qualifiedByName = "longToObject")
    Collection mapToDomain(CollectionDAO toMap,
                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default CollectionId fromLongToObject(CollectionDAO collectionDAO) {
        return CollectionId.of(collectionDAO.getId());
    }
}