package org.banta.xardhr.service.document.impl;

import org.banta.xardhr.dto.request.DocumentDto;
import org.banta.xardhr.service.document.DocumentService;

import java.util.List;

public class DefaultDocumentService implements DocumentService {

    @Override
    public DocumentDto uploadDocument(Long userId, DocumentDto document) {
        return null;
    }

    @Override
    public void deleteDocument(Long id) {

    }

    @Override
    public List<DocumentDto> getUserDocuments(Long userId) {
        return List.of();
    }
}
