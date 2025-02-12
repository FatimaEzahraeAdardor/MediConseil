package org.youcode.mediconseil.web.api.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.service.DoctorService;
import org.youcode.mediconseil.web.vm.mapper.DoctorMapper;
import org.youcode.mediconseil.web.vm.request.DoctorRequestVm;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    public DoctorController(DoctorService doctorService, DoctorMapper doctorMapper) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }
    @PostMapping("save")
    public ResponseEntity<Map<String , Object>> save(@RequestBody DoctorRequestVm doctorRequestVm) {
        Doctor doctor = doctorMapper.toEntity(doctorRequestVm );
        Doctor savedDoctor = doctorService.save(doctor);
        Map<String , Object> response = new HashMap<>();
        response.put("message", "doctor created successfully");
        response.put("doctor", savedDoctor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
