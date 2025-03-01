package org.youcode.mediconseil.web.api.consultation;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
        response.put("consultation", responseVm);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateConsultation(
            @PathVariable UUID id,
            @Valid @RequestBody ConsultationRequestVm consultationRequest) {

        Consultation consultation = consultationMapper.toEntity(consultationRequest);
        consultation.setId(id);

        Consultation updatedConsultation = consultationService.update(consultation, consultationRequest.getAvailabilityId());
        ConsultationResponseVm responseVm = consultationMapper.toVm(updatedConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation updated successfully");
        response.put("consultation", responseVm);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseVm> findById(@PathVariable UUID id) {
        Consultation consultation = consultationService.findByID(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        ConsultationResponseVm responseVm = consultationMapper.toVm(consultation);
        return ResponseEntity.ok(responseVm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        consultationService.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Consultation deleted successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Page<ConsultationResponseVm>> getAllConsultations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Consultation> consultations = consultationService.getAllConsultationsPaginated(page, size);
        Page<ConsultationResponseVm> responseVms = consultations.map(consultationMapper::toVm);

        return ResponseEntity.ok(responseVms);
    }
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelConsultation(@PathVariable UUID id) {
        Consultation canceledConsultation = consultationService.cancelConsultation(id);
        ConsultationResponseVm responseVm = consultationMapper.toVm(canceledConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation canceled successfully");
        response.put("consultation", responseVm);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Map<String, Object>> confirmConsultation(@PathVariable UUID id) {
        Consultation confirmedConsultation = consultationService.confirmConsultation(id);
        ConsultationResponseVm responseVm = consultationMapper.toVm(confirmedConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation confirmed successfully");
        response.put("consultation", responseVm);

        return ResponseEntity.ok(response);
    }
}