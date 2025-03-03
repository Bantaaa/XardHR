package org.banta.xardhr.service.document.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.Document;
import org.banta.xardhr.domain.enums.DocumentType;
import org.banta.xardhr.dto.request.DocumentDto;
import org.banta.xardhr.repository.DocumentRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.document.DocumentService;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultDocumentService implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir:./document-uploads}")
    private String uploadDirPath;

    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDirPath).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // Test write permissions
            Path testFile = uploadPath.resolve("test.txt");
            Files.createFile(testFile);
            Files.delete(testFile);

            log.info("Upload directory initialized at: {}", uploadPath);
        } catch (IOException e) {
            log.error("Could not initialize upload directory. Check permissions.", e);
        }
    }

    @Override
    public DocumentDto uploadDocument(Long userId, DocumentDto documentDto) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Document document = new Document();
        document.setAppUser(user);
        document.setTitle(documentDto.getTitle());
        document.setType(DocumentType.valueOf(documentDto.getType())); // Parse string to enum
        document.setFileUrl(documentDto.getFileUrl());

        // Parse dates if provided, otherwise use current date
        LocalDate uploadDate = documentDto.getUploadDate() != null ?
                LocalDate.parse(documentDto.getUploadDate()) :
                LocalDate.now();
        document.setUploadDate(uploadDate);

        if (documentDto.getExpiryDate() != null) {
            document.setExpiryDate(LocalDate.parse(documentDto.getExpiryDate()));
        }

        document.setIsVerified(documentDto.getIsVerified() != null ?
                documentDto.getIsVerified() : false);

        Document saved = documentRepository.save(document);
        return convertToDto(saved);
    }

    @Override
    public DocumentDto uploadDocumentWithFile(Long userId, String title, String type, MultipartFile file) {
        try {
            AppUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

            Path uploadPath = Paths.get(uploadDirPath).toAbsolutePath().normalize();
            Path userPath = uploadPath.resolve(userId.toString());
            Files.createDirectories(userPath);

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Save the file
            Path targetLocation = userPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.debug("File saved to: {}", targetLocation);

            // Create document record
            Document document = new Document();
            document.setAppUser(user);
            document.setTitle(title);
            document.setType(DocumentType.valueOf(type));
            document.setFileUrl("/document-uploads/" + userId + "/" + uniqueFilename);
            document.setUploadDate(LocalDate.now());
            document.setIsVerified(false);

            Document saved = documentRepository.save(document);

            DocumentDto dto = convertToDto(saved);
            dto.setFileSize(formatFileSize(file.getSize()));

            return dto;
        } catch (ResourceNotFoundException e) {
            // Simply rethrow resource not found exceptions
            throw e;
        } catch (Exception e) {
            // Log the detailed error and throw a user-friendly message
            log.error("Error uploading document", e);
            throw new RuntimeException("Failed to upload document: " + e.getMessage(), e);
        }
    }

    public Resource downloadDocument(Long documentId) {
        try {
            Document document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + documentId));

            // Extract the file name from the URL
            String fileUrl = document.getFileUrl();
            String relativePath = fileUrl.substring(fileUrl.indexOf("/document-uploads/") + 18); // Strip the prefix

            Path filePath = Paths.get(uploadDirPath).resolve(relativePath).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("File not found due to invalid URL format");
        }
    }

    @Override
    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));

        // Extract the file path from the URL
        try {
            String fileUrl = document.getFileUrl();
            String relativePath = fileUrl.substring(fileUrl.indexOf("/document-uploads/") + 18); // Strip the prefix

            Path filePath = Paths.get(uploadDirPath).resolve(relativePath).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Could not delete file for document ID: {}. Error: {}", id, e.getMessage());
            // Continue with database deletion even if file deletion fails
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

    private DocumentDto convertToDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId().toString());
        dto.setTitle(document.getTitle());
        dto.setType(document.getType().toString());
        dto.setFileUrl(document.getFileUrl());
        dto.setUploadDate(document.getUploadDate() != null ? document.getUploadDate().toString() : null);
        dto.setExpiryDate(document.getExpiryDate() != null ? document.getExpiryDate().toString() : null);
        dto.setIsVerified(document.getIsVerified());

        // Add fields needed by frontend
        AppUser user = document.getAppUser();
        dto.setEmployeeId(user.getEmployeeId().toString());
        dto.setEmployeeName(user.getFirstName() + " " + user.getLastName());

        // Set a dummy file size if not provided (frontend expects this)
        if (dto.getFileSize() == null) {
            dto.setFileSize("1.5 MB");
        }

        return dto;
    }

    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        }

        DecimalFormat df = new DecimalFormat("#.##");
        float sizeKb = 1.0f * size / 1024;
        if (sizeKb < 1024) {
            return df.format(sizeKb) + " KB";
        }

        float sizeMb = sizeKb / 1024;
        if (sizeMb < 1024) {
            return df.format(sizeMb) + " MB";
        }

        return df.format(sizeMb / 1024) + " GB";
    }
}