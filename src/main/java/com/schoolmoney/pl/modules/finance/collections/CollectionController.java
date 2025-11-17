package com.schoolmoney.pl.modules.finance.collections;

import com.schoolmoney.pl.modules.finance.collections.models.CollectionPageResponse;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionRequest;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionCreateService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionDeleteService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionEditService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionGetService;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionCreateService collectionCreateService;
    private final CollectionGetService collectionGetService;
    private final CollectionEditService collectionEditService;
    private final CollectionDeleteService collectionDeleteService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @PostMapping("/")
    @Operation(
            description = "Add collection",
            summary = "Add collection"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> addCollection(
            @RequestParam(name = "classId")UUID classId,
            @RequestBody @Valid CollectionRequest request
    ) {
        collectionCreateService.create(request, classId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @Operation(
            description = "Get user collections",
            summary = "Get user collections"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<CollectionPageResponse>> getCollections(
            @RequestParam(name = "limit", required = false, defaultValue = "10")int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1")int page,
            @RequestParam(name = "isTreasurer", required = false, defaultValue = "false")boolean isTreasurer
    ){
        CollectionPageResponse response = collectionGetService.get(limit, page, isTreasurer);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PutMapping("/{collectionId}")
    @Operation(
            description = "Edit collection",
            summary = "Edit collection"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editCollection(
            @PathVariable UUID collectionId,
            @RequestBody @Valid CollectionRequest request
    ) {
        collectionEditService.edit(request, collectionId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @DeleteMapping("/{collectionId}")
    @Operation(
            description = "Delete collection",
            summary = "Delete collection"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteCollection(
            @PathVariable UUID collectionId
    ) {
        collectionDeleteService.delete(collectionId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }
}
