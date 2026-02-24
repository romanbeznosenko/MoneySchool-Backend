package com.schoolmoney.pl.modules.finance.collections;

import com.schoolmoney.pl.modules.finance.attachments.models.AttachmentResponse;
import com.schoolmoney.pl.modules.finance.attachments.services.AttachmentDeleteService;
import com.schoolmoney.pl.modules.finance.attachments.services.AttachmentDownloadService;
import com.schoolmoney.pl.modules.finance.attachments.services.AttachmentGetService;
import com.schoolmoney.pl.modules.finance.attachments.services.AttachmentUploadService;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionId;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionPageResponse;
import com.schoolmoney.pl.modules.finance.collections.models.CollectionRequest;
import com.schoolmoney.pl.modules.finance.collections.models.TreasurerContributionRequest;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionCloseService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionCreateService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionDeleteService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionEditService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionGetService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionReportService;
import com.schoolmoney.pl.modules.finance.collections.services.CollectionTransferService;
import com.schoolmoney.pl.modules.finance.collections.services.TreasurerContributionService;
import com.schoolmoney.pl.modules.finance.refunds.models.RefundRequest;
import com.schoolmoney.pl.modules.finance.refunds.services.RefundService;
import com.schoolmoney.pl.modules.finance.contributions.models.CollectionPaymentSummaryResponse;
import com.schoolmoney.pl.modules.finance.contributions.services.CollectionPaymentSummaryService;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionCloseService collectionCloseService;
    private final CollectionCreateService collectionCreateService;
    private final CollectionGetService collectionGetService;
    private final CollectionEditService collectionEditService;
    private final CollectionDeleteService collectionDeleteService;
    private final CollectionReportService collectionReportService;
    private final TreasurerContributionService treasurerContributionService;
    private final CollectionPaymentSummaryService collectionPaymentSummaryService;
    private final CollectionTransferService collectionTransferService;
    private final RefundService refundService;
    private final AttachmentUploadService attachmentUploadService;
    private final AttachmentGetService attachmentGetService;
    private final AttachmentDownloadService attachmentDownloadService;
    private final AttachmentDeleteService attachmentDeleteService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @PostMapping("/")
    @Operation(
            description = "Add collection",
            summary = "Add collection"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<CollectionId>> addCollection(
            @RequestParam(name = "classId")UUID classId,
            @RequestBody @Valid CollectionRequest request
    ) {
        UUID collectionId = collectionCreateService.create(request, classId);

        return new ResponseEntity<>(new CustomResponse<>(CollectionId.of(collectionId), DEFAULT_RESPONSE, HttpStatus.CREATED),
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

    @GetMapping("/{collectionId}/payment-summary")
    @Operation(
            description = "Get payment summary with per-student breakdown for a collection",
            summary = "Get collection payment summary"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<CollectionPaymentSummaryResponse>> getPaymentSummary(
            @PathVariable UUID collectionId
    ) {
        CollectionPaymentSummaryResponse response = collectionPaymentSummaryService
                .getPaymentSummary(collectionId);

        return new ResponseEntity<>(
                new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @PostMapping("/{collectionId}/transfer-to-treasurer")
    @Operation(
            description = "Transfer collection funds to treasurer account",
            summary = "Transfer to treasurer"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> transferToTreasurer(
            @PathVariable UUID collectionId
    ) {
        collectionTransferService.transferToTreasurer(collectionId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PostMapping("/{collectionId}/refund")
    @Operation(
            description = "Refund parent for a student in collection",
            summary = "Refund parent"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> refundParent(
            @PathVariable UUID collectionId,
            @Valid @RequestBody RefundRequest refundRequest
    ) {
        refundService.processRefund(collectionId, refundRequest);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PostMapping("/{collectionId}/attachments")
    @Operation(
            description = "Upload attachments to collection (max 5 files: PDF, Word, Excel, Images)",
            summary = "Upload attachments"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> uploadAttachments(
            @PathVariable UUID collectionId,
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {
        attachmentUploadService.uploadAttachments(collectionId, files);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @GetMapping("/{collectionId}/attachments")
    @Operation(
            description = "Get list of attachments for a collection",
            summary = "Get attachments"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<List<AttachmentResponse>>> getAttachments(
            @PathVariable UUID collectionId
    ) {
        List<AttachmentResponse> attachments = attachmentGetService.getAttachments(collectionId);

        return new ResponseEntity<>(new CustomResponse<>(attachments, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @GetMapping("/{collectionId}/attachments/{attachmentId}/download")
    @Operation(
            description = "Download attachment file",
            summary = "Download attachment"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable UUID collectionId,
            @PathVariable UUID attachmentId
    ) {
        return attachmentDownloadService.downloadAttachment(attachmentId);
    }

    @DeleteMapping("/{collectionId}/attachments/{attachmentId}")
    @Operation(
            description = "Delete attachment from collection",
            summary = "Delete attachment"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteAttachment(
            @PathVariable UUID collectionId,
            @PathVariable UUID attachmentId
    ) {
        attachmentDeleteService.deleteAttachment(collectionId, attachmentId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PostMapping("/{collectionId}/close")
    @Operation(
            description = "Close collection with auto-refund to all parents",
            summary = "Close collection"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> closeCollection(
            @PathVariable UUID collectionId
    ) {
        collectionCloseService.closeCollection(collectionId);

        return new ResponseEntity<>(new CustomResponse<>(null, "Collection closed. All payments have been refunded.", HttpStatus.OK),
                HttpStatus.OK);
    }

    @PostMapping("/{collectionId}/treasurer-contribution")
    @Operation(
            description = "Treasurer contributes own money to collection",
            summary = "Treasurer contribution"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> treasurerContribution(
            @PathVariable UUID collectionId,
            @RequestBody @Valid TreasurerContributionRequest treasurerContributionRequest
    ) {
        treasurerContributionService.treasurerContribute(
                collectionId,
                treasurerContributionRequest.amount(),
                treasurerContributionRequest.note()
        );

        return new ResponseEntity<>(new CustomResponse<>(null, "Treasurer contribution successful!", HttpStatus.OK),
                HttpStatus.OK);
    }

    @GetMapping("/{collectionId}/report")
    @Operation(
            description = "Generate PDF report for collection",
            summary = "Generate collection report"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> generateReport(
            @PathVariable UUID collectionId
    ) {
        byte[] pdfBytes = collectionReportService.generatePdfReport(collectionId);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=collection-report-" + collectionId + ".pdf")
                .body(pdfBytes);
    }
}
