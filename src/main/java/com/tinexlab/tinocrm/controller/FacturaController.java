package com.tinexlab.tinocrm.controller;

import com.tinexlab.tinocrm.model.dto.request.FacturaRequest;
import com.tinexlab.tinocrm.service.FacturaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/facturas")
    /*public ResponseEntity<Page<Factura>> listarFacturas (@PageableDefault(page=0, size=5) Pageable pageable){
        return ResponseEntity.ok(facturaRepository.findAll(pageable));
    }*/
    public ResponseEntity<Object> listarFacturas(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return facturaService.getFacturas(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/facturas/{id}")
    public ResponseEntity<?> buscarFactura(@PathVariable Long id){
        return facturaService.getFactura(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PostMapping("/facturas")
    public ResponseEntity<?> guardarFactura(@Valid @RequestBody FacturaRequest facturaRequest, BindingResult result) {
        return facturaService.saveFactura(facturaRequest, result);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @PutMapping("/facturas/{id}")
    public ResponseEntity<?> editarFactura(@Valid @RequestBody FacturaRequest facturaRequest, BindingResult result, @PathVariable Long id){
        return facturaService.updateFactura(facturaRequest, result, id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @DeleteMapping("/facturas/{id}")
    public ResponseEntity<?> borrarFactura(@PathVariable Long id){
        return facturaService.deleteFactura(id);
    }

}
