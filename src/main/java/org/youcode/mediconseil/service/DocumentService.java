package org.youcode.mediconseil.service;

import org.springframework.web.multipart.MultipartFile;
import org.youcode.mediconseil.domain.Document;
import org.youcode.mediconseil.web.vm.request.DocumentRequestVm;

import java.util.UUID;

public interface DocumentService {
    Document createDocument(DocumentRequestVm documentRequestVm, MultipartFile file);
    byte[] downloadDocument(UUID documentId);
}