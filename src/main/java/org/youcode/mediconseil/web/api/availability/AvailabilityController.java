package org.youcode.mediconseil.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.service.AvailabilityService;
import org.youcode.mediconseil.web.vm.mapper.AvailabilityMapper;
import org.youcode.mediconseil.web.vm.request.AvailabilityRequestVm;
import org.youcode.mediconseil.web.vm.response.AvailabilityResponseVm;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final AvailabilityMapper availabilityMapper;

    @GetMapping
    public ResponseEntity<Page<AvailabilityResponseVm>> getAllAvailabilities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Availability> availabilitiesPage = availabilityService.getAllAvailabilitiesPaginated(page, size);
        return ResponseEntity.ok(availabilitiesPage.map(availabilityMapper::toVm));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseVm> getAvailabilityById(@PathVariable UUID id) {
        return availabilityService.findById(id).map(availabilityMapper::toVm)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AvailabilityResponseVm> createAvailability(@RequestBody AvailabilityRequestVm requestVm) {
        Availability availability = availabilityMapper.toEntity(requestVm);
        Availability savedAvailability = availabilityService.save(availability);
        return ResponseEntity.ok(availabilityMapper.toVm(savedAvailability));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseVm> updateAvailability(
            @PathVariable UUID id,
            @RequestBody AvailabilityRequestVm requestVm) {
        Availability availability = availabilityMapper.toEntity(requestVm);
        Availability updatedAvailability = availabilityService.update(id, availability);
        return ResponseEntity.ok(availabilityMapper.toVm(updatedAvailability));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable UUID id) {
        availabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AvailabilityResponseVm>> getAvailabilitiesByDoctor(@PathVariable UUID doctorId) {
        List<Availability> availabilities = availabilityService.getAvailabilitiesByDoctor(doctorId);
        List<AvailabilityResponseVm> responseVms = availabilities.stream()
                .map(availabilityMapper::toVm)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseVms);
    }

    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<AvailabilityResponseVm>> getAvailabilitiesByDoctorAndDate(
            @PathVariable UUID doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Availability> availabilities = availabilityService.getAvailabilitiesByDoctorAndDate(doctorId, date);
        List<AvailabilityResponseVm> responseVms = availabilities.stream()
                .map(availabilityMapper::toVm)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseVms);
    }

    @GetMapping("/doctor/{doctorId}/generate/{date}")
    public ResponseEntity<List<AvailabilityResponseVm>> getOrGenerateAvailabilitiesByDoctorAndDate(
            @PathVariable UUID doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Availability> availabilities = availabilityService.getOrGenerateAvailabilitiesByDoctorAndDate(doctorId, date);
        List<AvailabilityResponseVm> responseVms = availabilities.stream()
                .map(availabilityMapper::toVm)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseVms);
    }
}