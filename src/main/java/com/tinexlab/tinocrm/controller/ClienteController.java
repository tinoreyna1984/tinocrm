package com.tinexlab.tinocrm.controller;

import com.tinexlab.tinocrm.model.dto.request.ClienteRequest;
import com.tinexlab.tinocrm.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth") // toma el parámetro definido "bearerAuth" en el programa principal
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/clientes")
    /*public ResponseEntity<Page<Cliente>> listarClientes (@PageableDefault(page=0, size=5) Pageable pageable){
        return ResponseEntity.ok(clienteRepository.findAll(pageable));
    }*/
    public ResponseEntity<Object> listarClientes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return clienteService.getClientes(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> buscarCliente(@PathVariable Long id){
        return clienteService.getCliente(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PostMapping("/clientes")
    public ResponseEntity<?> guardarCliente(@Valid @RequestBody ClienteRequest clienteRequest, BindingResult result){
        return clienteService.saveCliente(clienteRequest, result);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> editarCliente(@Valid @RequestBody ClienteRequest clienteRequest, BindingResult result, @PathVariable Long id){
        return clienteService.updateCliente(clienteRequest, result, id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> borrarCliente(@PathVariable Long id){
        return clienteService.deleteCliente(id);
    }

}
