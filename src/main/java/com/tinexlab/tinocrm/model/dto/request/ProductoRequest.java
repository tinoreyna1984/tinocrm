package com.tinexlab.tinocrm.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequest {
    private String nombreProducto;
    private String descProducto;
    private Double precioProducto;
}
