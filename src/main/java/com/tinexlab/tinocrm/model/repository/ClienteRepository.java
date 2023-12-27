package com.tinexlab.tinocrm.model.repository;

import com.tinexlab.tinocrm.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
