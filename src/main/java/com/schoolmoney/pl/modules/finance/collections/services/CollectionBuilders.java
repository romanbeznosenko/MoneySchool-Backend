package com.schoolmoney.pl.modules.finance.collections.services;

import com.schoolmoney.pl.modules.finance.collections.models.Collection;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionId;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionRequest;
import com.schoolmoney.pl.modules.classes.models.Class;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionBuilders {
    public static Collection buildCollectionFromRequest(CollectionRequest request, Class aClass){
        return Collection.builder()
                .collectionId(CollectionId.of(null))
                .title(request.title())
                .description(request.description())
                .goal(request.goal())
                .logo(null)
                .aClass(aClass)
                .isArchived(false)
                .build();
    }
}
