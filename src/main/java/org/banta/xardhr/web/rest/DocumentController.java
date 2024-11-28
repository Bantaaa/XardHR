package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.DocumentDto;
import org.banta.xardhr.service.document.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<DocumentDto> uploadDocument(
            @RequestParam Long userId,
            @RequestBody DocumentDto document) {
        return ResponseEntity.ok(documentService.uploadDocument(userId, document));
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
}