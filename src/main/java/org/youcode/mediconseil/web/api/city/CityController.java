package org.youcode.mediconseil.web.api.city;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.service.CityService;
import org.youcode.mediconseil.web.vm.mapper.CityMapper;
import org.youcode.mediconseil.web.vm.request.CityRequestVM;
import org.youcode.mediconseil.web.vm.response.CityResponseVM;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;
    private final CityMapper cityMapper;

    public CityController(CityService cityService, CityMapper cityMapper) {
        this.cityService = cityService;
        this.cityMapper = cityMapper;
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> addCity(@RequestBody @Valid CityRequestVM cityRequestVM){
        City city = cityMapper.toEntity(cityRequestVM);
        City createdCity = cityService.save(city);
        CityResponseVM cityResponseVM = cityMapper.toVM(createdCity);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "city created successfully");
        response.put("category", cityResponseVM);
        return new  ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
