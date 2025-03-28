package org.youcode.mediconseil.web.api.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.youcode.mediconseil.domain.Document;
import org.youcode.mediconseil.service.DocumentService;
import org.youcode.mediconseil.web.vm.request.DocumentRequestVm;


import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<Document> createDocument(@ModelAttribute DocumentRequestVm documentRequestVm,
                                                   @RequestParam("file") MultipartFile file) {
        Document document = documentService.createDocument(documentRequestVm, file);
        return ResponseEntity.ok(document);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable UUID documentId) {
        byte[] fileContent = documentService.downloadDocument(documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document.pdf\"")
                .body(fileContent);
    }

}