package com.tinexlab.tinocrm.model.repository;

import com.tinexlab.tinocrm.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // lista de productos vendidos
    @Query("SELECT p.nombreProducto AS producto, COUNT(*) AS numVentas " +
            "FROM Producto p, Factura f " +
            "WHERE p.id = f.producto.id " +
            "GROUP BY p.nombreProducto " +
            "ORDER BY COUNT(*) DESC")
    List<Map<String, Object>> findProductosVendidos();

    // total recaudado según las facturas
    @Query("SELECT SUM(p.precioProducto) " +
            "FROM Producto p, Factura f " +
            "WHERE p.id = f.producto.id")
    BigDecimal findTotalVendido();
}
