package com.tinexlab.tinocrm.controller;

import com.tinexlab.tinocrm.model.dto.request.VentaRequest;
import com.tinexlab.tinocrm.model.entity.Cliente;
import com.tinexlab.tinocrm.model.entity.Factura;
import com.tinexlab.tinocrm.model.entity.Venta;
import com.tinexlab.tinocrm.model.repository.ClienteRepository;
import com.tinexlab.tinocrm.model.repository.FacturaRepository;
import com.tinexlab.tinocrm.model.repository.VentaRepository;
import com.tinexlab.tinocrm.model.entity.User;
import com.tinexlab.tinocrm.model.repository.UserRepository;
import com.tinexlab.tinocrm.service.VentaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/ventas")
    // devuelve una lista completa o paginada si viajan parámetros de paginación
    public ResponseEntity<Object> listarVentas(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ventaService.getVentas(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/ventas/{id}")
    public ResponseEntity<?> buscarVenta(@PathVariable Long id){
        return ventaService.getVenta(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/ventas/next-id-venta")
    public ResponseEntity<?> siguienteVentaId(){
        return ventaService.getNextVentaId();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PostMapping("/ventas")
    public ResponseEntity<?> guardarVenta(@Valid @RequestBody VentaRequest ventaRequest, BindingResult result) {
        return ventaService.saveVenta(ventaRequest, result);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PutMapping("/ventas/{id}")
    public ResponseEntity<?> editarVenta(@Valid @RequestBody VentaRequest ventaRequest, BindingResult result, @PathVariable Long id){
        return ventaService.updateVenta(ventaRequest, result, id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @DeleteMapping("/ventas/{id}")
    public ResponseEntity<?> borrarVenta(@PathVariable Long id){
        return ventaService.deleteVenta(id);
    }

}
