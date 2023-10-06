package com.tinexlab.tinocrm.security.controller;

import com.tinexlab.tinocrm.security.entity.User;
import com.tinexlab.tinocrm.security.repository.UserRepository;
import com.tinexlab.tinocrm.security.util.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // codificador de password
    @Autowired
    private PasswordEncoder passwordEncoder;

    private void encriptarClaveUsuario(User usuario) {
        String claveEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(claveEncriptada);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/users")
    public ResponseEntity<Page<User>> listarUsuarios (@PageableDefault(page=0, size=5) Pageable pageable){
        return ResponseEntity.ok(userRepository.findAll(pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/users/{id}")
    public ResponseEntity<?> buscarUsuario(@PathVariable Long id){
        User usuario = null;
        Map<String, Object> response = new HashMap<>();

        try {
            usuario = userRepository.findById(id).get();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<User>(usuario, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("/users")
    public ResponseEntity<?> guardarUsuario(@Valid @RequestBody User usuario, BindingResult result){
        User usuarioNuevo = null;
        Map<String, Object> response = new HashMap<>();

        // si no viaja el ROL, por defecto debe ser el de USUARIO
        if(usuario.getRole() == null)
            usuario.setRole(Role.USER);

        // proceso de validación
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        // encripta clave
        encriptarClaveUsuario(usuario);

        try {
            usuarioNuevo = userRepository.save(usuario);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido creado con éxito");
        response.put("usuario", usuarioNuevo);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> editarUsuario(@Valid @RequestBody User usuario, BindingResult result, @PathVariable Long id){
        User usuarioActual = userRepository.findById(id).get();
        User usuarioEditado = null;
        Map<String, Object> response = new HashMap<>();

        // proceso de validación
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            usuarioActual.setName(usuario.getName());
            usuarioActual.setLastName(usuario.getLastName());
            usuarioActual.setEmail(usuario.getEmail());
            usuarioActual.setUsername(usuario.getUsername());
            // encripta clave
            encriptarClaveUsuario(usuario);
            usuarioActual.setPassword(usuario.getPassword());
            // si no viaja el ROL, por defecto debe ser el de USUARIO
            if(usuario.getRole() == null)
                usuario.setRole(Role.USER);
            else
                usuarioActual.setRole(usuario.getRole());
            usuarioEditado = userRepository.save(usuarioActual);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido editado con éxito");
        response.put("usuario", usuarioEditado);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> borrarUsuario(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        try {
            userRepository.deleteById(id);
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el delete en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido eliminado con éxito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }
}
