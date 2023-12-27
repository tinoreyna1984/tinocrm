package com.tinexlab.tinocrm.service;

import com.tinexlab.tinocrm.model.dto.request.UserRequest;
import com.tinexlab.tinocrm.model.entity.User;
import com.tinexlab.tinocrm.model.repository.UserRepository;
import com.tinexlab.tinocrm.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // codificador de password
    @Autowired
    private PasswordEncoder passwordEncoder;

    private void encriptarClaveUsuario(UserRequest userRequest) {
        String claveEncriptada = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(claveEncriptada);
    }

    public ResponseEntity<Object> getUsuarios(Integer page, Integer size){
        if (page != null && size != null) {
            // Si se proporcionan los parámetros de paginación, devuelve una lista paginada
            Pageable pageable = PageRequest.of(page, size);
            Page<User> pageResult = userRepository.findAll(pageable);
            return ResponseEntity.ok(pageResult);
        } else {
            // Si no se proporcionan los parámetros de paginación, devuelve una lista completa
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        }
    }

    public ResponseEntity<?> getUsuario(Long id){
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

    public ResponseEntity<?> saveUsuario(UserRequest userRequest, BindingResult result){
        Map<String, Object> response = new HashMap<>();

        // si no viaja el ROL, por defecto debe ser el de USUARIO
        if(userRequest.getRole() == null)
            userRequest.setRole(Role.USER);

        // proceso de validación
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        User usuario = new User();
        // encripta clave
        encriptarClaveUsuario(userRequest);
        usuario.setName(userRequest.getName());
        usuario.setLastName(userRequest.getLastName());
        usuario.setUsername(userRequest.getUsername());
        usuario.setPassword(userRequest.getPassword());
        usuario.setEmail(userRequest.getEmail());
        usuario.setRole(userRequest.getRole());

        try {
            usuario = userRepository.save(usuario);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El usuario ha sido creado con éxito");
        response.put("usuario", usuario);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateUsuario(UserRequest userRequest, BindingResult result, Long id){
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
            usuarioActual.setName(userRequest.getName());
            usuarioActual.setLastName(userRequest.getLastName());
            usuarioActual.setEmail(userRequest.getEmail());
            usuarioActual.setUsername(userRequest.getUsername());
            // encripta clave
            encriptarClaveUsuario(userRequest);
            usuarioActual.setPassword(userRequest.getPassword());
            // si no viaja el ROL, por defecto debe ser el de USUARIO
            if(userRequest.getRole() == null)
                usuarioActual.setRole(Role.USER);
            else
                usuarioActual.setRole(userRequest.getRole());
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

    public ResponseEntity<?> deleteUsuario(Long id){
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
