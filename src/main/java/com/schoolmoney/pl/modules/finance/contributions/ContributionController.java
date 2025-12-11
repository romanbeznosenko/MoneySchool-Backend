package com.schoolmoney.pl.modules.finance.contributions;

import com.schoolmoney.pl.modules.finance.contributions.models.ContributionCreateRequest;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionListResponse;
import com.schoolmoney.pl.modules.finance.contributions.models.ContributionResponse;
import com.schoolmoney.pl.modules.finance.contributions.services.ContributionCreateService;
import com.schoolmoney.pl.modules.finance.contributions.services.ContributionGetService;
import com.schoolmoney.pl.modules.finance.contributions.services.UserContributionHistoryService;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contribution")
@RequiredArgsConstructor
public class ContributionController {
    private final ContributionCreateService contributionCreateService;
    private final ContributionGetService contributionGetService;
    private final UserContributionHistoryService userContributionHistoryService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @PostMapping("/collection/{collectionId}")
    @Operation(
            description = "Create a contribution (payment) for a collection",
            summary = "Create contribution"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ContributionResponse>> createContribution(
            @PathVariable UUID collectionId,
            @RequestBody @Valid ContributionCreateRequest request
    ) {
        ContributionResponse response = contributionCreateService.createContribution(
                collectionId,
                request
        );

        return new ResponseEntity<>(
                new CustomResponse<>(response, "Contribution created successfully", HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/collection/{collectionId}")
    @Operation(
            description = "Get all contributions for a collection",
            summary = "Get collection contributions"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ContributionListResponse>> getCollectionContributions(
            @PathVariable UUID collectionId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        ContributionListResponse response = contributionGetService.getContributionsByCollection(
                collectionId,
                pageable
        );

        return new ResponseEntity<>(
                new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @GetMapping("/collection/{collectionId}/student/{studentId}")
    @Operation(
            description = "Get contributions for a specific student in a collection",
            summary = "Get student contributions"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<List<ContributionResponse>>> getStudentContributions(
            @PathVariable UUID collectionId,
            @PathVariable UUID studentId
    ) {
        List<ContributionResponse> response = contributionGetService
                .getContributionsByCollectionAndStudent(collectionId, studentId);

        return new ResponseEntity<>(
                new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @GetMapping("/my-contributions")
    @Operation(
            description = "Get authenticated user's contribution history",
            summary = "Get my contributions"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ContributionListResponse>> getMyContributions(
            @RequestParam(name = "collectionId", required = false) UUID collectionId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        ContributionListResponse response = userContributionHistoryService.getUserContributions(
                collectionId,
                pageable
        );

        return new ResponseEntity<>(
                new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK
        );
    }
}
