package org.youcode.mediconseil.web.api.consultation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.service.ConsultaionService;
import org.youcode.mediconseil.web.vm.mapper.ConsultationMapper;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/consultations")
class ConsultationController {
    private final ConsultaionService consultationService;
    private final ConsultationMapper consultationMapper;

    ConsultationController(ConsultaionService consultationService, ConsultationMapper consultationMapper) {
        this.consultationService = consultationService;
        this.consultationMapper = consultationMapper;
    }
//    @PostMapping("/save")
//    public ResponseEntity<Map<String, Object>> createConsultation(@Valid @RequestBody ConsultationRequestVm request) {
//        Consultation consultation = consultationMapper.toEntity(request);
//        Consultation savedConsultation = consultationService.save(consultation);
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "consultation created successfully");
//        response.put("Consultation", savedConsultation);
//        return new  ResponseEntity<>(response, HttpStatus.CREATED);
//    }
}
