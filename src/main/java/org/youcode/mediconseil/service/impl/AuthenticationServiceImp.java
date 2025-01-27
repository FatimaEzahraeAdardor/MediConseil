package org.youcode.mediconseil.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.domain.enums.Role;
import org.youcode.mediconseil.repository.UserRepository;
import org.youcode.mediconseil.security.JwtService;
import org.youcode.mediconseil.service.AuthService;
import org.youcode.mediconseil.web.vm.request.AuthenticationRequest;
import org.youcode.mediconseil.web.vm.request.RegisterRequest;
import org.youcode.mediconseil.web.vm.response.AuthenticationResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthService {
    private final UserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .userName(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        appUserRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
