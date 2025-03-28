package org.youcode.mediconseil.service;

import org.youcode.mediconseil.web.vm.request.AuthenticationRequest;
import org.youcode.mediconseil.web.vm.request.RegisterRequest;
import org.youcode.mediconseil.web.vm.response.AuthenticationResponse;

public interface AuthService {
        AuthenticationResponse register(RegisterRequest request);
        AuthenticationResponse authenticate(AuthenticationRequest request);
}
