package org.youcode.mediconseil.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.youcode.mediconseil.domain.Article;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.repository.ArticleRepository;
import org.youcode.mediconseil.repository.CategoryRepository;
import org.youcode.mediconseil.repository.DoctorRepository;
import org.youcode.mediconseil.service.ArticleService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public Article save(Article article) {
        // Validate category
        Category category = categoryRepository.findById(article.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Validate doctor
        Doctor doctor = doctorRepository.findById(article.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Set validated entities
        article.setCategory(category);
        article.setDoctor(doctor);

        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public Article update(Article article) {
        // Find existing article
        Article existingArticle = articleRepository.findById(article.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        // Update article details
        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
        existingArticle.setImage(article.getImage());

        // Update category if provided
        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingArticle.setCategory(category);
        }

        // Update doctor if provided
        if (article.getDoctor() != null) {
            Doctor doctor = doctorRepository.findById(article.getDoctor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
            existingArticle.setDoctor(doctor);
        }

        return articleRepository.save(existingArticle);
    }

    @Override
    @Transactional
    public Boolean delete(UUID id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article not found");
        }
        articleRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Article> findById(UUID id) {
        return articleRepository.findById(id);
    }

    @Override
    public Page<Article> getAllArticlesPaginated(int page, int size) {
        return articleRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> getAllArticlesByDoctorId(UUID doctorId, int page, int size) {
        // Verify doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found");
        }

        return articleRepository.findByDoctorId(doctorId, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> getAllArticlesByCategoryId(UUID categoryId, int page, int size) {
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found");
        }

        return articleRepository.findByCategoryId(categoryId, PageRequest.of(page, size));
    }
}