package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Article;

import java.util.Optional;
import java.util.UUID;

public interface ArticleService {
    Article save(Article article);
    Article update(Article article);
    Boolean delete(UUID id);
    Optional<Article> findById(UUID id);
    Page<Article> getAllArticlesPaginated(int page, int size);
    Page<Article> getAllArticlesByDoctorId(UUID doctorId, int page, int size);
    Page<Article> getAllArticlesByCategoryId(UUID categoryId, int page, int size);

}
