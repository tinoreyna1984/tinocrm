package com.tinexlab.tinocrm.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinexlab.tinocrm.util.EstadoVenta;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VentaRequest {
    private String ordenVenta;
    private String descVenta;
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date fechaReserva;
    private Long clienteId;
    private Long facturaId; // requerido para la factura generada
    private Long userId;
}
