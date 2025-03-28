package org.youcode.mediconseil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.Document;
import org.youcode.mediconseil.domain.enums.DocumentType;

import java.util.List;
import java.util.UUID;
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByConsultation(Consultation consultation);
    List<Document> findByConsultationId(UUID consultationId);
    List<Document> findByType(DocumentType type);
}