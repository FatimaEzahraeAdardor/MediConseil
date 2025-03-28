package org.youcode.mediconseil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.Article;

import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Page<Article> findByDoctorId(UUID doctorId, Pageable pageable);
    Page<Article> findByCategoryId(UUID categoryId, Pageable pageable);
}