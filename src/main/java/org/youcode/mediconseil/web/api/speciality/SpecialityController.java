package org.youcode.mediconseil.web.api.speciality;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.service.SpecialityService;
import org.youcode.mediconseil.web.vm.mapper.SpecialityMapper;
import org.youcode.mediconseil.web.vm.request.CityRequestVM;
import org.youcode.mediconseil.web.vm.request.SpecialityRequest;
import org.youcode.mediconseil.web.vm.response.CategoryResponseVM;
import org.youcode.mediconseil.web.vm.response.CityResponseVM;
import org.youcode.mediconseil.web.vm.response.SpecialityResponseVm;

import java.util.*;

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
    @GetMapping("/{id}")
    public ResponseEntity<SpecialityResponseVm> findById(@PathVariable UUID id) {
        Optional<Speciality> specialityOptional = specialityService.findById(id);
        SpecialityResponseVm specialityResponseVm = specialityMapper.toVM(specialityOptional.get());
        return ResponseEntity.ok(specialityResponseVm);
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String > delete(@PathVariable UUID id) {
        specialityService.delete(id);
        return ResponseEntity.ok("Deleted speciality successfully");
    }

    @GetMapping()
    public ResponseEntity<Page<SpecialityResponseVm>> getAllSpecialities(@RequestParam int page, @RequestParam int size) {
        Page<Speciality> specialities =specialityService.getAllSpecialitiesPaginated(page,size);
        List<SpecialityResponseVm> specialityResponseVms =specialities.getContent().stream().map(specialityMapper::toVM).toList();
        Page<SpecialityResponseVm> specialityResponseVmPage = new PageImpl<>(specialityResponseVms);
        return ResponseEntity.ok(specialityResponseVmPage);
    }

}
