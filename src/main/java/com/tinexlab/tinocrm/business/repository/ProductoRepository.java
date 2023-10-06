package com.tinexlab.tinocrm.business.repository;

import com.tinexlab.tinocrm.business.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
