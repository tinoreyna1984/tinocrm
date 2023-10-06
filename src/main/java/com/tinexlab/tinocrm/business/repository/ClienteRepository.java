package com.tinexlab.tinocrm.business.repository;

import com.tinexlab.tinocrm.business.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
