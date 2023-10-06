package com.tinexlab.tinocrm.business.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequest {
    private String nombreProducto;
    private String descProducto;
    private Double precioProducto;
}
