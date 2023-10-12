package com.tinexlab.tinocrm.security.service;

import com.tinexlab.tinocrm.security.dto.AuthenticationRequest;
import com.tinexlab.tinocrm.security.dto.AuthenticationResponse;
import com.tinexlab.tinocrm.security.dto.RegistrationRequest;
import com.tinexlab.tinocrm.security.entity.User;
import com.tinexlab.tinocrm.security.repository.UserRepository;
import com.tinexlab.tinocrm.security.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse login(AuthenticationRequest authRequest) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()
        );

        authenticationManager.authenticate(authToken);

        User user = userRepository.findByUsername(authRequest.getUsername()).get();

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse register(RegistrationRequest registrationRequest){
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(registrationRequest.getPassword());
        user.setEmail(registrationRequest.getEmail());
        user.setName(registrationRequest.getName());
        user.setLastName(registrationRequest.getLastName());
        if(registrationRequest.getRole() == null)
            user.setRole(Role.USER);
        else
            user.setRole(registrationRequest.getRole());
        userRepository.save(user);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(jwt);
    }

    private Map<String, Object> generateExtraClaims(User user) {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId()); //
        extraClaims.put("name", user.getName());
        extraClaims.put("lastName", user.getLastName()); //
        extraClaims.put("email", user.getEmail()); //
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("permissions", user.getAuthorities());

        return extraClaims;
    }
}
