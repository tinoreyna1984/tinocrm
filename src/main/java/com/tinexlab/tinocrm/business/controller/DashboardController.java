package com.tinexlab.tinocrm.business.controller;

import com.tinexlab.tinocrm.business.entity.Producto;
import com.tinexlab.tinocrm.business.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private ProductoRepository productoRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/productos-vendidos")
    public ResponseEntity<?> listaProductosVendidos(){
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> productosVendidos = null;
        try{
            productosVendidos = productoRepository.findProductosVendidos();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(productosVendidos, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/total-vendido")
    public ResponseEntity<?> mostrarTotalVendido(){
        Map<String, Object> response = new HashMap<>();
        BigDecimal totalVendido = BigDecimal.valueOf(0);
        try{
            totalVendido = productoRepository.findTotalVendido();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("totalVendido", totalVendido);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
