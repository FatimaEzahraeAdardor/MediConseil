package org.youcode.mediconseil.web.api.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.service.UserService;
import org.youcode.mediconseil.web.vm.request.RegisterRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> updateSpeciality(@PathVariable("id") UUID id, @RequestBody @Valid RegisterRequest registerRequest){
        User savedUser = userService.update(id, registerRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "speciality updated successfully");
        response.put("speciality", savedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
