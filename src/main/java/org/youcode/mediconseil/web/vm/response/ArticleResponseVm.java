package org.youcode.mediconseil.web.vm.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseVm {
    private UUID id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private UUID categoryId;
    private UUID doctorId;
}