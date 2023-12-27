package com.tinexlab.tinocrm.controller;

import com.tinexlab.tinocrm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/productos-vendidos")
    public ResponseEntity<?> listaProductosVendidos(){
        return dashboardService.getProductosVendidos();
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/total-vendido")
    public ResponseEntity<?> mostrarTotalVendido(){
        return dashboardService.showTotalVendido();
    }
}
