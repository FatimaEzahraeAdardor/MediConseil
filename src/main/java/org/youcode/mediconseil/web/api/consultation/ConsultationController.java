package org.youcode.mediconseil.web.api.consultation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.service.ConsultaionService;
import org.youcode.mediconseil.web.vm.mapper.ConsultationMapper;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;
import org.youcode.mediconseil.web.vm.response.ConsultationResponseVm;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultations")
class ConsultationController {
    private final ConsultaionService consultationService;
    private final ConsultationMapper consultationMapper;

    ConsultationController(ConsultaionService consultationService, ConsultationMapper consultationMapper) {
        this.consultationService = consultationService;
        this.consultationMapper = consultationMapper;
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookConsultation(
            @Valid @RequestBody ConsultationRequestVm consultationRequestVm) {

        Consultation consultation = consultationMapper.toEntity(consultationRequestVm);
        Consultation bookedConsultation = consultationService.save(consultation, consultationRequestVm.getAvailabilityId());
        ConsultationResponseVm responseVm = consultationMapper.toVm(bookedConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation created successfully");
        response.put("consultation", bookedConsultation);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
