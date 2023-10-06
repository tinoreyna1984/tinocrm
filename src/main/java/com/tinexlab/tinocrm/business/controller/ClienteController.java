package com.tinexlab.tinocrm.business.controller;

import com.tinexlab.tinocrm.business.dto.ClienteRequest;
import com.tinexlab.tinocrm.business.entity.Cliente;
import com.tinexlab.tinocrm.business.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/clientes")
    // por defecto quiero que comience en la primera página (se cuenta desde cero)
    // y que se muestre 5 elementos por página.
    // Es norma para todos los GET de los controladores (aunque pueda variar en el parámetro size)
    public ResponseEntity<Page<Cliente>> listarClientes (@PageableDefault(page=0, size=5) Pageable pageable){
        return ResponseEntity.ok(clienteRepository.findAll(pageable));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> buscarCliente(@PathVariable Long id){
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();

        try {
            cliente = clienteRepository.findById(id).get();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PostMapping("/clientes")
    public ResponseEntity<?> guardarCliente(@Valid @RequestBody ClienteRequest clienteRequest, BindingResult result){
        Cliente clienteNuevo = null;
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
            Cliente cliente = new Cliente();
            cliente.setNombreCliente(clienteRequest.getNombreCliente());
            cliente.setApellidosCliente(clienteRequest.getApellidosCliente());
            cliente.setDocId(clienteRequest.getDocId());
            cliente.setTipoDoc(clienteRequest.getTipoDoc());
            cliente.setEstadoCliente(clienteRequest.getEstadoCliente());
            cliente.setFonoCliente(clienteRequest.getFonoCliente());
            cliente.setEmailCliente(clienteRequest.getEmailCliente());
            clienteNuevo = clienteRepository.save(cliente);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido creado con éxito");
        response.put("cliente", clienteNuevo);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> editarCliente(@Valid @RequestBody ClienteRequest clienteRequest, BindingResult result, @PathVariable Long id){
        Cliente clienteActual = clienteRepository.findById(id).get();
        Cliente clienteEditado = null;
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
            clienteActual.setNombreCliente(clienteRequest.getNombreCliente());
            clienteActual.setApellidosCliente(clienteRequest.getApellidosCliente());
            clienteActual.setDocId(clienteRequest.getDocId());
            clienteActual.setTipoDoc(clienteRequest.getTipoDoc());
            clienteActual.setEstadoCliente(clienteRequest.getEstadoCliente());
            clienteActual.setFonoCliente(clienteRequest.getFonoCliente());
            clienteActual.setEmailCliente(clienteRequest.getEmailCliente());
            clienteEditado = clienteRepository.save(clienteActual);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido editado con éxito");
        response.put("cliente", clienteEditado);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> borrarCliente(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();

        try {
            clienteRepository.deleteById(id);
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el delete en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido eliminado con éxito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }

}
