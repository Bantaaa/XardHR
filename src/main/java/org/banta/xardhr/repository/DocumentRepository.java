package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.appUser.id = ?1")
    List<Document> findByUserId(Long userId);

    @Query("SELECT d FROM Document d ORDER BY d.uploadDate DESC")
    List<Document> findAllOrderByUploadDateDesc();
}