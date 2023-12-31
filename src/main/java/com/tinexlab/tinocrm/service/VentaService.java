package com.tinexlab.tinocrm.service;

import com.tinexlab.tinocrm.model.dto.request.VentaRequest;
import com.tinexlab.tinocrm.model.entity.Cliente;
import com.tinexlab.tinocrm.model.entity.Factura;
import com.tinexlab.tinocrm.model.entity.User;
import com.tinexlab.tinocrm.model.entity.Venta;
import com.tinexlab.tinocrm.model.repository.ClienteRepository;
import com.tinexlab.tinocrm.model.repository.FacturaRepository;
import com.tinexlab.tinocrm.model.repository.UserRepository;
import com.tinexlab.tinocrm.model.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> getVentas(Integer page, Integer size){
        // se obtienen los valores de autenticación para verificar el usuario y su rol
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){
            // se busca usuario por username
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).get();

            // verifica rol
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"))) {
                // si el usuario es administrador, mostrar todos los registros sin filtro
                if (page != null && size != null) {
                    Pageable pageable = PageRequest.of(page, size);
                    Page<Venta> pageResult = ventaRepository.findAll(pageable);
                    return ResponseEntity.ok(pageResult);
                } else {
                    List<Venta> ventas = ventaRepository.findAll();
                    return ResponseEntity.ok(ventas);
                }
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                // si el usuario es usuario, mostrar solo sus registros
                if (page != null && size != null) {
                    Pageable pageable = PageRequest.of(page, size);
                    Page<Venta> pageResult = ventaRepository.findByUser(user, pageable);
                    return ResponseEntity.ok(pageResult);
                } else {
                    List<Venta> ventas = ventaRepository.findByUser(user);
                    return ResponseEntity.ok(ventas);
                }
            } else {
                // para cualquier otro rol
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        // si no se halla el usuario
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<?> getVenta(Long id){
        Venta venta = null;
        Map<String, Object> response = new HashMap<>();

        try {
            venta = ventaRepository.findById(id).get();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Venta>(venta, HttpStatus.OK);
    }

    public ResponseEntity<?> getNextVentaId(){
        Map<String, Object> response = new HashMap<>();
        Long nextId = 0L;
        try{
            nextId = ventaRepository.getNextId();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(nextId);
    }

    public ResponseEntity<?> saveVenta(VentaRequest ventaRequest, BindingResult result){
        Map<String, Object> response = new HashMap<>();

        // Proceso de validación
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        // Buscar objetos requeridos por su ID en la base de datos
        Cliente cliente = clienteRepository.findById(ventaRequest.getClienteId()).orElse(null);
        Factura factura = facturaRepository.findById(ventaRequest.getFacturaId()).orElse(null);
        User user = userRepository.findById(ventaRequest.getUserId()).orElse(null);
        if (cliente == null) {
            response.put("mensaje", "Cliente no encontrado con el ID: " + ventaRequest.getClienteId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (factura == null) {
            response.put("mensaje", "Factura no encontrado con el ID: " + ventaRequest.getFacturaId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (user == null) {
            response.put("mensaje", "Usuario no encontrado con el ID: " + ventaRequest.getUserId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        // Crear una nueva factura
        Venta ventaNueva = new Venta();
        ventaNueva.setOrdenVenta(ventaRequest.getOrdenVenta());
        ventaNueva.setDescVenta(ventaRequest.getDescVenta());
        ventaNueva.setEstadoVenta(ventaRequest.getEstadoVenta());
        ventaNueva.setFechaReserva(ventaRequest.getFechaReserva());
        ventaNueva.setCliente(cliente);
        ventaNueva.setFactura(factura);
        ventaNueva.setUser(user);

        try {
            // Guardar la factura en la base de datos
            ventaNueva = ventaRepository.save(ventaNueva);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La venta ha sido creada con éxito");
        response.put("venta", ventaNueva);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateVenta(VentaRequest ventaRequest, BindingResult result, Long id){
        Venta ventaActual = ventaRepository.findById(id).get();
        Venta ventaEditada = null;
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

        // Buscar objetos requeridos por su ID en la base de datos
        Cliente cliente = clienteRepository.findById(ventaRequest.getClienteId()).orElse(null);
        Factura factura = facturaRepository.findById(ventaRequest.getFacturaId()).orElse(null);
        User user = userRepository.findById(ventaRequest.getUserId()).orElse(null);
        if (cliente == null) {
            response.put("mensaje", "Cliente no encontrado con el ID: " + ventaRequest.getClienteId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (factura == null) {
            response.put("mensaje", "Factura no encontrado con el ID: " + ventaRequest.getFacturaId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (user == null) {
            response.put("mensaje", "Usuario no encontrado con el ID: " + ventaRequest.getUserId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            ventaActual.setOrdenVenta(ventaRequest.getOrdenVenta());
            ventaActual.setDescVenta(ventaRequest.getDescVenta());
            ventaActual.setEstadoVenta(ventaRequest.getEstadoVenta());
            ventaActual.setFechaReserva(ventaRequest.getFechaReserva());
            ventaActual.setCliente(cliente);
            ventaActual.setFactura(factura);
            ventaActual.setUser(user);
            ventaEditada = ventaRepository.save(ventaActual);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La venta ha sido editada con éxito");
        response.put("venta", ventaEditada);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> deleteVenta(Long id){
        Map<String, Object> response = new HashMap<>();

        try {
            ventaRepository.deleteById(id);
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el delete en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La venta ha sido eliminada con éxito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }
}
