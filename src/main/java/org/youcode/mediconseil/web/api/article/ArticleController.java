package org.youcode.mediconseil.web.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.Article;
import org.youcode.mediconseil.service.ArticleService;
import org.youcode.mediconseil.web.vm.mapper.ArticleMapper;
import org.youcode.mediconseil.web.vm.request.ArticleRequestVm;
import org.youcode.mediconseil.web.vm.response.ArticleResponseVm;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    @PostMapping("save")
    public ResponseEntity<Map<String, Object>> createArticle(
            @Valid @RequestBody ArticleRequestVm articleRequestVm) {

        Article article = articleMapper.toEntity(articleRequestVm);
        Article savedArticle = articleService.save(article);
        ArticleResponseVm responseVm = articleMapper.toVm(savedArticle);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Article created successfully");
        response.put("article", responseVm);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateArticle(
            @PathVariable UUID id,
            @Valid @RequestBody ArticleRequestVm articleRequestVm) {

        Article article = articleMapper.toEntity(articleRequestVm);
        article.setId(id);
        Article updatedArticle = articleService.update(article);
        ArticleResponseVm responseVm = articleMapper.toVm(updatedArticle);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Article updated successfully");
        response.put("article", responseVm);

        return ResponseEntity.ok(response);
    }

}