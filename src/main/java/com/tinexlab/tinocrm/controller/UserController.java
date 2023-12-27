package com.tinexlab.tinocrm.controller;

import com.tinexlab.tinocrm.model.dto.request.UserRequest;
import com.tinexlab.tinocrm.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/users")
    // devuelve una lista completa o paginada si viajan parámetros de paginación
    public ResponseEntity<Object> listarUsuarios(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return userService.getUsuarios(page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/users/{id}")
    public ResponseEntity<?> buscarUsuario(@PathVariable Long id){
        return userService.getUsuario(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("/users")
    public ResponseEntity<?> guardarUsuario(@Valid @RequestBody UserRequest userRequest, BindingResult result){
        return userService.saveUsuario(userRequest, result);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> editarUsuario(@Valid @RequestBody UserRequest userRequest, BindingResult result, @PathVariable Long id){
        return userService.updateUsuario(userRequest, result, id);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> borrarUsuario(@PathVariable Long id){
        return userService.deleteUsuario(id);
    }
}
