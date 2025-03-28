package org.youcode.mediconseil.web.api.city;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.service.CityService;
import org.youcode.mediconseil.web.vm.mapper.CityMapper;
import org.youcode.mediconseil.web.vm.request.CityRequestVM;
import org.youcode.mediconseil.web.vm.response.CategoryResponseVM;
import org.youcode.mediconseil.web.vm.response.CityResponseVM;

import java.util.*;

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
        response.put("city", cityResponseVM);
        return new  ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> updateCity(@PathVariable("id") UUID id, @RequestBody @Valid CityRequestVM cityRequestVM){
        City city = cityMapper.toEntity(cityRequestVM);
        City updateCity = cityService.update(id ,city);
        CityResponseVM cityResponseVM = cityMapper.toVM(updateCity);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "city updated successfully");
        response.put("city", cityResponseVM);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable("id") UUID id){
        cityService.delete(id);
        return ResponseEntity.ok("city deleted successfully");
    }
    @GetMapping("/{id}")
    public ResponseEntity<CityResponseVM> findById(@PathVariable UUID id) {
        Optional<City> city = cityService.findById(id);
        CityResponseVM cityResponseVM = cityMapper.toVM(city.get());
        return ResponseEntity.ok(cityResponseVM);
    }
    @GetMapping("list")
    public ResponseEntity<List<CityResponseVM>> findAllCities() {
        List<City> cityList = cityService.findAllCities();
        List<CityResponseVM> cityResponseVMSlist = cityList.stream().map(cityMapper::toVM).toList();
        return ResponseEntity.ok(cityResponseVMSlist);
    }
    @GetMapping("all")
    public ResponseEntity<Page<CityResponseVM>> findAll(@RequestParam int page, @RequestParam int size) {
        Page<City> cityPage = cityService.findAll(page, size);
        Page<CityResponseVM> cityResponseVMPage = cityPage.map(cityMapper::toVM);
        return ResponseEntity.ok(cityResponseVMPage);

    }
    @GetMapping("/region/{region}")
    public ResponseEntity<Page<CityResponseVM>> findCitiesByRegion(@PathVariable String region, @RequestParam int page, @RequestParam int size) {
        Page<City> cityPage = cityService.findAllByRegion(region,page,size);
        Page<CityResponseVM> cityResponseVMPage = cityPage.map(cityMapper::toVM);
        return ResponseEntity.ok(cityResponseVMPage);
    }
    @GetMapping("/regions")
    public ResponseEntity<List<String>> getAllRegions() {
        List<String> regions = cityService.getAllRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

}
