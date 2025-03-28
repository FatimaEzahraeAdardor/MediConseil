package org.youcode.mediconseil.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.domain.enums.Role;
import org.youcode.mediconseil.repository.UserRepository;
import org.youcode.mediconseil.service.CityService;
import org.youcode.mediconseil.service.DoctorService;
import org.youcode.mediconseil.service.SpecialityService;
import org.youcode.mediconseil.service.UserService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;
import org.youcode.mediconseil.web.vm.request.RegisterRequest;

import java.util.Optional;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CityService cityService;
    private final DoctorService doctorService;
    private final SpecialityService specialityService;
    public UserServiceImpl(UserRepository userRepository, CityService cityService, DoctorService doctorService, SpecialityService specialityService) {
        this.userRepository = userRepository;
        this.cityService = cityService;
        this.doctorService = doctorService;
        this.specialityService = specialityService;
    }
    @Override
    public User save(User User) {
        return null;
    }

    @Override
    public User update(UUID userId, RegisterRequest user) {
        // Fetch existing user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Update city if provided and not null
        if (user.getCity_id() != null) {
            City city = cityService.findById(user.getCity_id())
                    .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + user.getCity_id()));
            existingUser.setCity(city);
        }


        // Update general user details
        existingUser.setUserName(user.getUserName() != null ? user.getUserName() : existingUser.getUsername());
        existingUser.setFirstName(user.getFirstName() != null ? user.getFirstName() : existingUser.getFirstName());
        existingUser.setLastName(user.getLastName() != null ? user.getLastName() : existingUser.getLastName());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : existingUser.getPhoneNumber());

        if (user.getPassword() != null) {
            existingUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }

        // Save updated user
        existingUser = userRepository.save(existingUser);
        return existingUser;
    }





    @Override
    public Boolean delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        userRepository.delete(user);
        return true;
    }

    @Override
    public Optional<User> findByID(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional;
        } else {
            throw new ResourceNotFoundException("user not found");
        }
    }
    @Override
    public Page<User> getAllUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getAllPatientsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllPatients(pageable);
    }
}
