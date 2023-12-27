package com.tinexlab.tinocrm.service;

import com.tinexlab.tinocrm.model.dto.request.ClienteRequest;
import com.tinexlab.tinocrm.model.entity.Cliente;
import com.tinexlab.tinocrm.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ResponseEntity<Object> getClientes(Integer page, Integer size) {
        if (page != null && size != null) {
            // Si se proporcionan los parámetros de paginación, devuelve una lista paginada
            Pageable pageable = PageRequest.of(page, size);
            Page<Cliente> pageResult = clienteRepository.findAll(pageable);
            return ResponseEntity.ok(pageResult);
        } else {
            // Si no se proporcionan los parámetros de paginación, devuelve una lista completa
            List<Cliente> clientes = clienteRepository.findAll();
            return ResponseEntity.ok(clientes);
        }
    }

    public ResponseEntity<?> getCliente(Long id){
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

    public ResponseEntity<?> saveCliente(ClienteRequest clienteRequest, BindingResult result){
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

    public ResponseEntity<?> updateCliente(ClienteRequest clienteRequest, BindingResult result, Long id){
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

    public ResponseEntity<?> deleteCliente(Long id){
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
