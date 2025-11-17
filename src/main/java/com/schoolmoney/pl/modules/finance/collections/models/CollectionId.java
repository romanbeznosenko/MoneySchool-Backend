package com.schoolmoney.pl.modules.finance.collections.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class CollectionId {
    UUID id;
}
