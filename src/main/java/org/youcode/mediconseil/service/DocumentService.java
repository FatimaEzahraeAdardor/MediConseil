package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Article;
import org.youcode.mediconseil.domain.Document;

import java.util.Optional;
import java.util.UUID;

public interface DocumentService {
    Document save(Document document);
    Document update(Document document);
    Boolean delete(UUID id);
    Optional<Document> findById(UUID id);
    Page<Document> getAllDocumentsPaginated(int page, int size);

}
