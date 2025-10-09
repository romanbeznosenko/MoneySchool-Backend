package com.schoolmoney.pl.core.student;

import com.schoolmoney.pl.core.student.models.StudentCreateRequest;
import com.schoolmoney.pl.core.student.models.StudentEditRequest;
import com.schoolmoney.pl.core.student.models.StudentGetPageResponse;
import com.schoolmoney.pl.core.student.service.StudentCreateService;
import com.schoolmoney.pl.core.student.service.StudentDeleteService;
import com.schoolmoney.pl.core.student.service.StudentEditService;
import com.schoolmoney.pl.core.student.service.StudentGetService;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
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
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentCreateService studentCreateService;
    private final StudentGetService studentGetService;
    private final StudentEditService studentEditService;
    private final StudentDeleteService studentDeleteService;

    private final static String DEFAULT_MESSAGE = "Operation successful.";

    @PostMapping(value = "/create")
    @Operation(
            description = "Add student",
            summary = "Add student"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> createStudent(
            @RequestBody StudentCreateRequest request
    ) {
        studentCreateService.createStudent(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @Operation(
            description = "Get user students",
            summary = "Get user students"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<StudentGetPageResponse>> getUserStudents(
            @RequestParam(name = "page", required = false, defaultValue = "1")int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10")int limit
    ){
        StudentGetPageResponse response = studentGetService.getUserStudents(page, limit);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PutMapping("/{studentId}")
    @Operation(
            description = "Edit student",
            summary = "Edit student"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editStudent(
            @PathVariable UUID studentId,
            @RequestBody @Valid StudentEditRequest request
    ) throws UserNotFoundException
    {
        studentEditService.editStudent(studentId, request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    @Operation(
            description = "Delete student",
            summary = "Delete student"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteStudent(
            @PathVariable UUID studentId
    ){
        studentDeleteService.deleteStudent(studentId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }

}
