package com.schoolmoney.pl.modules.finance.collections.management;

import com.schoolmoney.pl.modules.finance.collections.models.CollectionDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollectionManager {
    private final CollectionRepository collectionRepository;

    public CollectionDAO saveToDatabase(CollectionDAO collection) {
        return collectionRepository.save(collection);
    }

    public Page<CollectionDAO> findAll(Specification<CollectionDAO> spec, Pageable pageable) {
        return collectionRepository.findAll(spec, pageable);
    }

    public Optional<CollectionDAO> findById(UUID id) {
        return collectionRepository.findById(id);
    }
}