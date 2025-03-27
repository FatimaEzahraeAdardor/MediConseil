package org.youcode.mediconseil.web.api.user;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.service.UserService;
import org.youcode.mediconseil.web.vm.mapper.PatientMapper;
import org.youcode.mediconseil.web.vm.request.RegisterRequest;
import org.youcode.mediconseil.web.vm.response.CityResponseVM;
import org.youcode.mediconseil.web.vm.response.PatientResponseVm;
import org.youcode.mediconseil.web.vm.response.SpecialityResponseVm;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final PatientMapper patientMapper;
    public UserController(UserService userService, PatientMapper patientMapper) {
        this.userService = userService;
        this.patientMapper = patientMapper;
    }
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> updateSpeciality(@PathVariable("id") UUID id, @RequestBody @Valid RegisterRequest registerRequest){
        User savedUser = userService.update(id, registerRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "user updated successfully");
        response.put("user", savedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable("id") UUID id){
        userService.delete(id);
        return ResponseEntity.ok("user deleted successfully");
    }
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseVm> findById(@PathVariable UUID id) {
        Optional<User> userOptional = userService.findByID(id);
        PatientResponseVm patientResponseVm = patientMapper.toVM(userOptional.get());
        return ResponseEntity.ok(patientResponseVm);
    }
    @GetMapping()
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam int page, @RequestParam int size) {
        Page<User> userPage =userService.getAllUsersPaginated(page,size);
        return ResponseEntity.ok(userPage);
    }
    @GetMapping("patients")
    public ResponseEntity<Page<PatientResponseVm>> getAllPatients(@RequestParam int page, @RequestParam int size) {
        Page<User> userPage =userService.getAllPatientsPaginated(page,size);
        List<PatientResponseVm> patientResponseVmList = userPage.getContent().stream().map(patientMapper::toVM).toList();
        Page<PatientResponseVm> patientResponseVmPage = new PageImpl<>(patientResponseVmList);
        return ResponseEntity.ok(patientResponseVmPage);
        }
}
