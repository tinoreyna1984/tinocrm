package com.tinexlab.tinocrm.security.controller;

import com.tinexlab.tinocrm.security.dto.AuthenticationRequest;
import com.tinexlab.tinocrm.security.dto.AuthenticationResponse;
import com.tinexlab.tinocrm.security.dto.RegistrationRequest;
import com.tinexlab.tinocrm.security.repository.UserRepository;
import com.tinexlab.tinocrm.security.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("permitAll")
    @PostMapping("/authenticate")
    /*public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authRequest){
        AuthenticationResponse jwtDto = authenticationService.login(authRequest);
        return ResponseEntity.ok(jwtDto);
    }*/
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest authRequest, BindingResult result){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            AuthenticationResponse jwtDto = authenticationService.login(authRequest);
            response.put("mensaje", "Se ha dado acceso al usuario");
            response.put("jwt", jwtDto.getJwt());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (DataAccessException e) {
            response.put("mensaje", "Error al registrar el usuario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (AuthenticationException e) {
            response.put("error", e.getMessage());
            response.put("mensaje", "Error en autenticación");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("permitAll")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest registrationRequest, BindingResult result){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if(userRepository.findByUsername(registrationRequest.getUsername()).isPresent()){
            response.put("mensaje", "El nombre de usuario " + registrationRequest.getUsername() +" ya está en uso");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            AuthenticationResponse jwtDto = authenticationService.register(registrationRequest);
            response.put("mensaje", "El usuario ha sido registrado con éxito");
            response.put("jwt", jwtDto.getJwt());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (DataAccessException e) {
            response.put("mensaje", "Error al registrar el usuario en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("permitAll")
    @GetMapping("/public-access")
    public String publicAccessEndpoint(){
        return "Este endpoint es público";
    }

}
