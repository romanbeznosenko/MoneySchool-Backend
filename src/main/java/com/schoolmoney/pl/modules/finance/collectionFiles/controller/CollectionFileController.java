package com.schoolmoney.pl.modules.finance.collectionFiles.controller;

import com.schoolmoney.pl.core.user.management.UserManager;
import com.schoolmoney.pl.core.user.management.UserNotFoundException;
import com.schoolmoney.pl.core.user.models.UserDAO;
import com.schoolmoney.pl.modules.finance.collectionFiles.model.CollectionFile;
import com.schoolmoney.pl.modules.finance.collectionFiles.service.CollectionFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collections")
public class CollectionFileController {

    private final CollectionFileService service;
    private final UserManager userManager;

    @PostMapping(
            value = "/{collectionId}/files",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> upload(
            @PathVariable UUID collectionId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UUID userID
    ) throws IOException, UserNotFoundException {
        UserDAO user = userManager.findUserById(userID).orElseThrow(UserNotFoundException::new);
        service.uploadPdf(collectionId, user, file);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{collectionId}/files")
    @PreAuthorize("permitAll()")
    public List<CollectionFile> list(
            @PathVariable UUID collectionId,
            @AuthenticationPrincipal UUID userID
    ) throws AccessDeniedException, UserNotFoundException {
        UserDAO user = userManager.findUserById(userID).orElseThrow(UserNotFoundException::new);
        return service.listFiles(collectionId, user);
    }

    @GetMapping("/{collectionId}/files/{fileId}/url")
    @PreAuthorize("permitAll()")
    public String getUrl(
            @PathVariable UUID collectionId,
            @PathVariable UUID fileId,
            @AuthenticationPrincipal UUID userID
    ) throws UserNotFoundException {
        UserDAO user = userManager.findUserById(userID).orElseThrow(UserNotFoundException::new);
        return service.getPresignedUrl(collectionId, fileId, user);
    }
}
