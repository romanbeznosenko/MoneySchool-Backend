package com.schoolmoney.pl.modules.finance.financeAccount;

import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountCreateRequest;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountEditRequest;
import com.schoolmoney.pl.modules.finance.financeAccount.models.FinanceAccountGetResponse;
import com.schoolmoney.pl.modules.finance.financeAccount.service.FinanceAccountCreateService;
import com.schoolmoney.pl.modules.finance.financeAccount.service.FinanceAccountEditService;
import com.schoolmoney.pl.modules.finance.financeAccount.service.FinanceAccountGetService;
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
@RequiredArgsConstructor
@RequestMapping("/api/finance-account")
public class FinanceAccountController {
    private static final String DEFAULT_RESPONSE = "Operation successful.";
    private final FinanceAccountGetService financeAccountGetService;
    private final FinanceAccountCreateService financeAccountCreateService;
    private final FinanceAccountEditService financeAccountEditService;

    @GetMapping("/")
    @Operation(
            description = "Get user finance account",
            summary = "Get user finance account"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<FinanceAccountGetResponse>> getFinanceAccount() {
        FinanceAccountGetResponse response = financeAccountGetService.getUserFinanceAccount();

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PostMapping("/")
    @Operation(
            description = "Create finance account",
            summary = "Create finance account"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> createFinanceAccount(
            @Valid @RequestBody FinanceAccountCreateRequest createRequest
    ) {
        financeAccountCreateService.createFinanceAccount(createRequest);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    @PutMapping("/{financeAccountId}")
    @Operation(
            description = "Edit finance account",
            summary = "Edit finance account"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editFinanceAccount(
            @PathVariable UUID financeAccountId,
            @Valid @RequestBody FinanceAccountEditRequest editRequest
    ) {
        financeAccountEditService.editFinanceAccount(editRequest, financeAccountId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }
}