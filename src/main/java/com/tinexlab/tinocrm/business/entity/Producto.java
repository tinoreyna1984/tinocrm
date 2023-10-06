package com.tinexlab.tinocrm.business.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "productos")
@Getter
@Setter
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long id;

    @Column(name = "nombre_producto")
    private String nombreProducto;
    @Column(name = "desc_producto")
    private String descProducto;

    @Column(name = "precio_producto", columnDefinition="Decimal(10,2) default '0.00'")
    private Double precioProducto;

    @OneToMany(mappedBy = "producto")
    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // previene la validación innecesaria del Content-Type
    private List<Factura> facturas;

}
