package org.youcode.mediconseil.web.vm.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.youcode.mediconseil.domain.enums.DocumentType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestVm {
    private String title;
    private String content;
    private DocumentType type;
    private UUID consultationId; // Only the consultation ID is needed
}