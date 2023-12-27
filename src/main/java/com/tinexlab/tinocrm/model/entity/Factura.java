package com.tinexlab.tinocrm.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinexlab.tinocrm.util.FormaPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "facturas")
@Getter
@Setter
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factura_id")
    private Long id;

    @Column(name = "cod_factura")
    private String codFactura;

    @Column(name = "forma_pago")
    @ColumnDefault("'EFECTIVO'")
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    @Column(name = "fecha_pago", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date fechaPago;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "producto_id", referencedColumnName = "producto_id")
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // previene la validación innecesaria del Content-Type
    private Producto producto;

    @OneToOne(mappedBy = "factura", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // previene la validación innecesaria del Content-Type
    private Venta venta;

    @Override
    public String toString() {
        return "Cód. Factura: " + this.codFactura + ", pagado el " + this.fechaPago.toString() + " con " + this.formaPago;
    }
}
