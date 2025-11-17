package com.schoolmoney.pl.modules.classMember;

import com.schoolmoney.pl.modules.classMember.models.ClassMemberGetPageResponse;
import com.schoolmoney.pl.modules.classMember.service.ClassMemberAddStudentService;
import com.schoolmoney.pl.modules.classMember.service.ClassMemberDeleteStudentService;
import com.schoolmoney.pl.modules.classMember.service.ClassMemberGetService;
import com.schoolmoney.pl.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api/class-member")
@RequiredArgsConstructor
public class ClassMemberController {
    private final ClassMemberAddStudentService classMemberAddStudentService;
    private final ClassMemberGetService classMemberGetService;
    private final ClassMemberDeleteStudentService classMemberDeleteStudentService;

    private final static String DEFAULT_MESSAGE = "Operation successful.";

    @PostMapping("/add")
    @Operation(
            description = "Add student to class",
            summary = "Add sdutent to class"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> addStudentToClass(
            @RequestParam(name = "classId")UUID classId,
            @RequestParam(name = "studentId")UUID studentId,
            @RequestParam(name = "accessCode")String accessCode
            ) throws NoSuchAlgorithmException, ClassNotFoundException {
        classMemberAddStudentService.addStudentToClass(classId, studentId, accessCode);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    @GetMapping("/{class-id}/list")
    @Operation(
            description = "Get students from class",
            summary = "Get students from class"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ClassMemberGetPageResponse>> getClassMembers(
            @PathVariable("class-id") UUID classId,
            @RequestParam(name = "page", required = false, defaultValue = "1")int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10")int limit
    ) throws ClassNotFoundException {
        ClassMemberGetPageResponse response = classMemberGetService.getClassMembers(page, limit, classId);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @DeleteMapping("/")
    @Operation(
            description = "Delete class member",
            summary = "Delete class member"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteClassMember(
            @RequestParam(name = "classMemberId")UUID classMemberId
    ) {
        classMemberDeleteStudentService.deleteClassMember(classMemberId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_MESSAGE, HttpStatus.OK),
                HttpStatus.OK);
    }
}
