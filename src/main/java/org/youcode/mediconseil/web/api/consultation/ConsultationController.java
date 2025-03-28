package org.youcode.mediconseil.web.api.consultation;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.service.ConsultationService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;
import org.youcode.mediconseil.web.vm.mapper.ConsultationMapper;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;
import org.youcode.mediconseil.web.vm.response.ConsultationResponseVm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/consultations")
class ConsultationController {
    private final ConsultationService consultationService;
    private final ConsultationMapper consultationMapper;

    ConsultationController(ConsultationService consultationService, ConsultationMapper consultationMapper) {
        this.consultationService = consultationService;
        this.consultationMapper = consultationMapper;
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookConsultation(
            @Valid @RequestBody ConsultationRequestVm consultationRequestVm) {

        Consultation bookedConsultation = consultationService.bookConsultation(consultationRequestVm);
        ConsultationResponseVm responseVm = consultationMapper.toResponseVm(bookedConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation created successfully");
        response.put("consultation", responseVm);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateConsultation(
            @PathVariable UUID id,
            @Valid @RequestBody ConsultationRequestVm consultationRequest) {

        // First find the existing consultation
        Consultation existingConsultation = consultationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with ID: " + id));

        // Update the fields that can be changed
        existingConsultation.setMotif(consultationRequest.getMotif());

        // Update the consultation
        Consultation updatedConsultation = consultationService.update(id, existingConsultation);
        ConsultationResponseVm responseVm = consultationMapper.toResponseVm(updatedConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation updated successfully");
        response.put("consultation", responseVm);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseVm> findById(@PathVariable UUID id) {
        Consultation consultation = consultationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with ID: " + id));
        ConsultationResponseVm responseVm = consultationMapper.toResponseVm(consultation);
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
    public ResponseEntity<List<ConsultationResponseVm>> getAllConsultations() {
        List<Consultation> consultations = consultationService.findAll();
        List<ConsultationResponseVm> responseVms = consultations.stream()
                .map(consultationMapper::toResponseVm)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseVms);
    }
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<ConsultationResponseVm>> getConsultationsByDoctor(
            @PathVariable UUID doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Consultation> consultations = consultationService.findByDoctorId(doctorId, page, size);
        Page<ConsultationResponseVm> responseVms = consultations.map(consultationMapper::toResponseVm);

        return ResponseEntity.ok(responseVms);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<ConsultationResponseVm>> getConsultationsByPatient(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Consultation> consultations = consultationService.findByPatientId(patientId, page, size);
        Page<ConsultationResponseVm> responseVms = consultations.map(consultationMapper::toResponseVm);

        return ResponseEntity.ok(responseVms);
    }


    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelConsultation(@PathVariable UUID id) {
        Consultation canceledConsultation = consultationService.cancelConsultation(id);
        ConsultationResponseVm responseVm = consultationMapper.toResponseVm(canceledConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation canceled successfully");
        response.put("consultation", responseVm);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Map<String, Object>> confirmConsultation(@PathVariable UUID id) {
        Consultation confirmedConsultation = consultationService.confirmConsultation(id);
        ConsultationResponseVm responseVm = consultationMapper.toResponseVm(confirmedConsultation);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Consultation confirmed successfully");
        response.put("consultation", responseVm);

        return ResponseEntity.ok(response);
    }
}