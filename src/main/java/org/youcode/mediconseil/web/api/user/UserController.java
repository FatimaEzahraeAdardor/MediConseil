package org.youcode.mediconseil.web.api.user;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.service.UserService;
import org.youcode.mediconseil.web.vm.request.RegisterRequest;


import java.util.*;

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
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        Optional<User> userOptional = userService.findByID(id);
        User user = userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(user);
    }
    @GetMapping()
    public ResponseEntity<Page<User>> getAllCategories(@RequestParam int page, @RequestParam int size) {
        Page<User> userPage =userService.getAllUsersPaginated(page,size);
        return ResponseEntity.ok(userPage);
    }
}
