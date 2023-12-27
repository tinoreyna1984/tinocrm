package com.tinexlab.tinocrm.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinexlab.tinocrm.util.FormaPago;
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
