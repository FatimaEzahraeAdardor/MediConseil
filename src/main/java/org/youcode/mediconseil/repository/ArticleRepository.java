package org.youcode.mediconseil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.Article;

import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

}
