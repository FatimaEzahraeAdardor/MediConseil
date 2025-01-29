package org.youcode.mediconseil.web.api.speciality;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.service.SpecialityService;
import org.youcode.mediconseil.web.vm.mapper.SpecialityMapper;
import org.youcode.mediconseil.web.vm.request.CityRequestVM;
import org.youcode.mediconseil.web.vm.request.SpecialityRequest;
import org.youcode.mediconseil.web.vm.response.CityResponseVM;
import org.youcode.mediconseil.web.vm.response.SpecialityResponseVm;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialities")
public class SpecialityController {
    private final SpecialityService specialityService;
    private final SpecialityMapper specialityMapper;
    public SpecialityController(SpecialityService specialityService, SpecialityMapper specialityMapper) {
        this.specialityService = specialityService;
        this.specialityMapper = specialityMapper;
    }
    @PostMapping("save")
    public ResponseEntity<Map<String , Object>> save(@RequestBody SpecialityRequest specialityRequest) {
        Speciality speciality = specialityMapper.toEntity(specialityRequest);
        Speciality savedSpeciality = specialityService.save(speciality);
        SpecialityResponseVm specialityResponseVm = specialityMapper.toVM(savedSpeciality);
        Map<String , Object> response = new HashMap<>();
        response.put("message", "speciality created successfully");
        response.put("speciality", specialityResponseVm);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> updateSpeciality(@PathVariable("id") UUID id, @RequestBody @Valid SpecialityRequest specialityRequest){
        Speciality speciality = specialityMapper.toEntity(specialityRequest);
        Speciality savedSpeciality = specialityService.update(id, speciality);
        SpecialityResponseVm specialityResponseVm = specialityMapper.toVM(savedSpeciality);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "speciality updated successfully");
        response.put("speciality", specialityResponseVm);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
