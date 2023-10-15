package com.tinexlab.tinocrm.business.controller;

import com.tinexlab.tinocrm.business.dto.VentaRequest;
import com.tinexlab.tinocrm.business.entity.Cliente;
import com.tinexlab.tinocrm.business.entity.Factura;
import com.tinexlab.tinocrm.business.entity.Venta;
import com.tinexlab.tinocrm.business.repository.ClienteRepository;
import com.tinexlab.tinocrm.business.repository.FacturaRepository;
import com.tinexlab.tinocrm.business.repository.VentaRepository;
import com.tinexlab.tinocrm.security.entity.User;
import com.tinexlab.tinocrm.security.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/ventas")
    /*public ResponseEntity<Page<Venta>> listarVentas (@PageableDefault(page=0, size=5) Pageable pageable){
        return ResponseEntity.ok(ventaRepository.findAll(pageable));
    }*/
    // devuelve una lista completa o paginada si viajan parámetros de paginación
    public ResponseEntity<Object> listarVentas(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            // Si se proporcionan los parámetros de paginación, devuelve una lista paginada
            Pageable pageable = PageRequest.of(page, size);
            Page<Venta> pageResult = ventaRepository.findAll(pageable);
            return ResponseEntity.ok(pageResult);
        } else {
            // Si no se proporcionan los parámetros de paginación, devuelve una lista completa
            List<Venta> ventas = ventaRepository.findAll();
            return ResponseEntity.ok(ventas);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/ventas/{id}")
    public ResponseEntity<?> buscarVenta(@PathVariable Long id){
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

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PostMapping("/ventas")
    public ResponseEntity<?> guardarVenta(@Valid @RequestBody VentaRequest ventaRequest, BindingResult result) {
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

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PutMapping("/ventas/{id}")
    public ResponseEntity<?> editarVenta(@Valid @RequestBody VentaRequest ventaRequest, BindingResult result, @PathVariable Long id){
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

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @DeleteMapping("/ventas/{id}")
    public ResponseEntity<?> borrarVenta(@PathVariable Long id){
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
