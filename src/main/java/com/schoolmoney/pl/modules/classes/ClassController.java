package com.schoolmoney.pl.modules.classes;

import com.schoolmoney.pl.modules.classes.models.ClassEditRequest;
import com.schoolmoney.pl.modules.classes.models.ClassGetPageResponse;
import com.schoolmoney.pl.modules.classes.service.ClassCreateService;
import com.schoolmoney.pl.modules.classes.service.ClassDeleteService;
import com.schoolmoney.pl.modules.classes.service.ClassEditService;
import com.schoolmoney.pl.modules.classes.service.ClassGetService;
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
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
    private final ClassCreateService classCreateService;
    private final ClassGetService classGetService;
    private final ClassDeleteService classDeleteService;
    private final ClassEditService classEditService;

    private final static String DEFAULT_MESSAGE = "Operation successful.";

    @PostMapping("/create")
    @Operation(
            description = "Create class",
            summary = "Create class"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> createClass(
            @RequestParam(name = "name") String name
    ) {
        classCreateService.createClass(name);
        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @Operation(
            description = "Get user classes",
            summary = "Get user classes"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ClassGetPageResponse>> getClasses(
            @RequestParam(name = "isTreasurer", required = false, defaultValue = "false")boolean isTreasurer,
            @RequestParam(name = "page", required = false, defaultValue = "1")int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10")int limit
    ){
        ClassGetPageResponse response = classGetService.getUserClasses(page, limit, isTreasurer);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @DeleteMapping("/{classId}")
    @Operation(
            description = "Delete class",
            summary = "Delete class"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteClass(
            @PathVariable UUID classId
    ) {
        classDeleteService.deleteClass(classId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PutMapping("/{classId}")
    @Operation(
            description = "Edit class",
            summary = "Edit class"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editClass(
            @PathVariable UUID classId,
            @RequestBody @Valid ClassEditRequest classEditRequest
    ) {
        classEditService.editClass(classId, classEditRequest);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }
}
