package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banta.xardhr.dto.request.DocumentDto;
import org.banta.xardhr.service.document.DocumentService;
import org.banta.xardhr.service.document.impl.DefaultDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<DocumentDto> uploadDocument(
            @RequestParam Long userId,
            @RequestBody DocumentDto document) {
        return ResponseEntity.ok(documentService.uploadDocument(userId, document));
    }

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<DocumentDto> uploadDocumentWithFile(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String type,
            @RequestParam("file") MultipartFile file) {

        log.debug("Received document upload request: userId={}, title={}, type={}, filename={}, fileSize={}",
                userId, title, type, file.getOriginalFilename(), file.getSize());

        DocumentDto uploadedDoc = documentService.uploadDocumentWithFile(
                userId, title, type, file
        );

        return ResponseEntity.ok(uploadedDoc);
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Resource resource = ((DefaultDocumentService)documentService).downloadDocument(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employee/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<DocumentDto>> getUserDocuments(@PathVariable Long userId) {
        return ResponseEntity.ok(documentService.getUserDocuments(userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        return ResponseEntity.ok(((DefaultDocumentService)documentService).getAllDocuments());
    }
}