package com.tinexlab.tinocrm.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinexlab.tinocrm.business.util.FormaPago;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FacturaRequest {
    private String codFactura;
    private FormaPago formaPago;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date fechaPago;
    private Long productoId;
}
