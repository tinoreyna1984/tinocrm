package com.tinexlab.tinocrm.service;

import com.tinexlab.tinocrm.model.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private ProductoRepository productoRepository;

    public ResponseEntity<?> getProductosVendidos(){
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

    public ResponseEntity<?> showTotalVendido(){
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
