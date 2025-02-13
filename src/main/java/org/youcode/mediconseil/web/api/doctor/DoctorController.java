package org.youcode.mediconseil.web.api.doctor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.service.DoctorService;
import org.youcode.mediconseil.web.vm.mapper.DoctorMapper;
import org.youcode.mediconseil.web.vm.request.DoctorRequestVm;
import org.youcode.mediconseil.web.vm.request.RegisterRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> updateDoctor(@PathVariable("id") UUID id, @RequestBody @Valid DoctorRequestVm doctorRequestVm){
        Doctor savedDoctor = doctorService.update(id, doctorRequestVm);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "doctor updated successfully");
        response.put("user", savedDoctor);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
