package org.youcode.mediconseil.web.api.doctor;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.service.DoctorService;
import org.youcode.mediconseil.web.vm.mapper.DoctorMapper;
import org.youcode.mediconseil.web.vm.request.DoctorRequestVm;
import org.youcode.mediconseil.web.vm.response.CityResponseVM;
import org.youcode.mediconseil.web.vm.response.DoctorResponseVm;

import java.util.*;

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
        DoctorResponseVm doctorResponseVm = doctorMapper.toVm(savedDoctor);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "doctor updated successfully");
        response.put("user", doctorResponseVm);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseVm> findById(@PathVariable UUID id) {
        Optional<Doctor> doctor = doctorService.findById(id);
        DoctorResponseVm doctorResponseVm = doctorMapper.toVm(doctor.get());
        return ResponseEntity.ok(doctorResponseVm);
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String > delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.ok("Deleted doctor successfully");
    }
    @GetMapping("all")
    public ResponseEntity<Page<DoctorResponseVm>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Doctor> doctorPage = doctorService.getAllDoctorsPaginated(page, size);
        Page<DoctorResponseVm> doctorResponseVms = doctorPage.map(doctorMapper::toVm);
        return ResponseEntity.ok(doctorResponseVms);
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<Page<DoctorResponseVm>> getDoctorsBySpecialty(
            @PathVariable UUID specialtyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialtyId, page, size);
        Page<DoctorResponseVm> responseVms = doctors.map(doctorMapper::toVm);

        return ResponseEntity.ok(responseVms);
    }

    @GetMapping("")
    public ResponseEntity<List<DoctorResponseVm>> findDoctors() {
        List<Doctor> doctors = doctorService.getDoctors();
        List<DoctorResponseVm> doctorResponseVmList = doctors.stream().map(doctorMapper::toVm).toList();
        return ResponseEntity.ok(doctorResponseVmList);
    }
    }
