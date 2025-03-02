package org.youcode.mediconseil.web.api.availability;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.service.AvailabiityService;
import org.youcode.mediconseil.web.vm.mapper.AvailabilityMapper;
import org.youcode.mediconseil.web.vm.request.AvailabilityRequestVm;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {
    private final AvailabiityService availabilityService;
    private final AvailabilityMapper availabilityMapper;
    public AvailabilityController(AvailabiityService availabiityService, AvailabilityMapper availabilityMapper) {
        this.availabilityService = availabiityService;
        this.availabilityMapper = availabilityMapper;
    }
    @PostMapping("save")
    public ResponseEntity<Availability> createAvailability(
            @Valid @RequestBody AvailabilityRequestVm request) {
        Availability availability = availabilityMapper.toEntity(request);
        return ResponseEntity.ok(availabilityService.save(availability));
    }


}
