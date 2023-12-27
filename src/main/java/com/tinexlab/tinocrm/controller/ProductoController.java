package com.tinexlab.tinocrm.controller;

import com.tinexlab.tinocrm.model.dto.request.ProductoRequest;
import com.tinexlab.tinocrm.model.repository.ProductoRepository;
import com.tinexlab.tinocrm.service.ProductoService;
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
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/productos")
    /*public ResponseEntity<Page<Producto>> listarProductos (@PageableDefault(page=0, size=5) Pageable pageable){
        return ResponseEntity.ok(productoRepository.findAll(pageable));
    }*/
    public ResponseEntity<Object> listarProductos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return productoService.getProductos(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR','ROLE_USER')")
    @GetMapping("/productos/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Long id){
        return productoService.getProducto(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("/productos")
    public ResponseEntity<?> guardarProducto(@Valid @RequestBody ProductoRequest productoRequest, BindingResult result){
        return productoService.saveProducto(productoRequest, result);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping("/productos/{id}")
    public ResponseEntity<?> editarProducto(@Valid @RequestBody ProductoRequest productoRequest, BindingResult result, @PathVariable Long id){
        return productoService.updateProducto(productoRequest, result, id);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> borrarProducto(@PathVariable Long id){
        return productoService.deleteProducto(id);
    }

}
