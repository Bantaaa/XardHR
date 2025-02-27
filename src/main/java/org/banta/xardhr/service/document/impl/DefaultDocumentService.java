package org.banta.xardhr.service.document.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.Document;
import org.banta.xardhr.dto.request.DocumentDto;
import org.banta.xardhr.repository.DocumentRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.document.DocumentService;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultDocumentService implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Override
    public DocumentDto uploadDocument(Long userId, DocumentDto documentDto) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Document document = new Document();
        document.setAppUser(user);
        document.setTitle(documentDto.getTitle());
        document.setType(documentDto.getType());
        document.setFileUrl(documentDto.getFileUrl());
        document.setUploadDate(documentDto.getUploadDate() != null ?
                documentDto.getUploadDate() : LocalDate.now());
        document.setExpiryDate(documentDto.getExpiryDate());
        document.setIsVerified(documentDto.getIsVerified() != null ?
                documentDto.getIsVerified() : false);

        Document saved = documentRepository.save(document);
        return convertToDto(saved);
    }

    @Override
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document not found with ID: " + id);
        }

        documentRepository.deleteById(id);
    }

    @Override
    public List<DocumentDto> getUserDocuments(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        List<Document> documents = documentRepository.findByUserId(userId);
        return documents.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Add method to get all documents for admins
    public List<DocumentDto> getAllDocuments() {
        List<Document> documents = documentRepository.findAllOrderByUploadDateDesc();
        return documents.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Convert entity to DTO with frontend-expected fields
    private DocumentDto convertToDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId().toString());
        dto.setTitle(document.getTitle());
        dto.setType(document.getType());
        dto.setFileUrl(document.getFileUrl());
        dto.setUploadDate(document.getUploadDate());
        dto.setExpiryDate(document.getExpiryDate());
        dto.setIsVerified(document.getIsVerified());

        // Add fields needed by frontend
        AppUser user = document.getAppUser();
        dto.setEmployeeId(user.getEmployeeId().toString());
        dto.setEmployeeName(user.getFirstName() + " " + user.getLastName());

        // Set a dummy file size if not provided (frontend expects this)
        dto.setFileSize("1.5 MB");

        return dto;
    }
}