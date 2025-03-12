package org.banta.xardhr.service.document;

import org.banta.xardhr.dto.request.DocumentDto;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DocumentService {
    DocumentDto uploadDocument(Long userId, DocumentDto document);

    DocumentDto uploadDocumentWithFile(Long userId, String title, String type, MultipartFile file);

    Resource downloadDocument(Long documentId);

    void deleteDocument(Long id);

    List<DocumentDto> getUserDocuments(Long userId);
}