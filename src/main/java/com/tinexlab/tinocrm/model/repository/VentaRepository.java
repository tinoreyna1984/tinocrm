package com.tinexlab.tinocrm.model.repository;

import com.tinexlab.tinocrm.model.entity.Venta;
import com.tinexlab.tinocrm.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Page<Venta> findByUser(User user, Pageable pageable);
    List<Venta> findByUser(User user);

    @Query(value = "SELECT AUTO_INCREMENT " +
            "FROM information_schema.TABLES " +
            "WHERE TABLE_SCHEMA = 'tinocrm' " +
            "AND TABLE_NAME = 'facturas'", nativeQuery = true) // solo para MySQL, modificar para otra base de datos
    Long getNextId();
}
