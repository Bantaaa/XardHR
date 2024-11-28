package org.banta.xardhr.service.document;

import org.banta.xardhr.dto.request.DocumentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {
    DocumentDto uploadDocument(Long userId, DocumentDto document);
    void deleteDocument(Long id);
    List<DocumentDto> getUserDocuments(Long userId);
}
