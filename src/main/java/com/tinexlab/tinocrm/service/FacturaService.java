package com.tinexlab.tinocrm.service;

import com.tinexlab.tinocrm.model.dto.request.FacturaRequest;
import com.tinexlab.tinocrm.model.entity.Factura;
import com.tinexlab.tinocrm.model.entity.Producto;
import com.tinexlab.tinocrm.model.repository.FacturaRepository;
import com.tinexlab.tinocrm.model.repository.ProductoRepository;
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
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public ResponseEntity<Object> getFacturas(Integer page, Integer size){
        if (page != null && size != null) {
            // Si se proporcionan los parámetros de paginación, devuelve una lista paginada
            Pageable pageable = PageRequest.of(page, size);
            Page<Factura> pageResult = facturaRepository.findAll(pageable);
            return ResponseEntity.ok(pageResult);
        } else {
            // Si no se proporcionan los parámetros de paginación, devuelve una lista completa
            List<Factura> facturas = facturaRepository.findAll();
            return ResponseEntity.ok(facturas);
        }
    }

    public ResponseEntity<?> getFactura(Long id){
        Factura factura = null;
        Map<String, Object> response = new HashMap<>();

        try {
            factura = facturaRepository.findById(id).get();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Factura>(factura, HttpStatus.OK);
    }

    public ResponseEntity<?> saveFactura(FacturaRequest facturaRequest, BindingResult result) {
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

        // Buscar el producto por su ID en la base de datos
        Producto producto = productoRepository.findById(facturaRequest.getProductoId()).orElse(null);

        if (producto == null) {
            response.put("mensaje", "Producto no encontrado con el ID: " + facturaRequest.getProductoId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        // Crear una nueva factura
        Factura facturaNueva = new Factura();
        facturaNueva.setCodFactura(facturaRequest.getCodFactura());
        facturaNueva.setFormaPago(facturaRequest.getFormaPago());
        facturaNueva.setFechaPago(facturaRequest.getFechaPago());
        facturaNueva.setProducto(producto);

        try {
            // Guardar la factura en la base de datos
            facturaNueva = facturaRepository.save(facturaNueva);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La factura ha sido creada con éxito");
        response.put("factura", facturaNueva);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateFactura(FacturaRequest facturaRequest, BindingResult result, Long id){
        Factura facturaActual = facturaRepository.findById(id).get();
        Factura facturaEditada = null;
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

        // Buscar el producto por su ID en la base de datos
        Producto producto = productoRepository.findById(facturaRequest.getProductoId()).orElse(null);
        if (producto == null) {
            response.put("mensaje", "Producto no encontrado con el ID: " + facturaRequest.getProductoId());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            facturaActual.setCodFactura(facturaRequest.getCodFactura());
            facturaActual.setFormaPago(facturaRequest.getFormaPago());
            facturaActual.setFechaPago(facturaRequest.getFechaPago());
            facturaActual.setProducto(producto);
            facturaEditada = facturaRepository.save(facturaActual);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La factura ha sido editada con éxito");
        response.put("factura", facturaEditada);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> deleteFactura(Long id){
        Map<String, Object> response = new HashMap<>();

        try {
            facturaRepository.deleteById(id);
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el delete en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La factura ha sido eliminada con éxito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }
}
