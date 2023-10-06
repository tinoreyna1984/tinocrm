package com.tinexlab.tinocrm.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tinexlab.tinocrm.business.util.EstadoVenta;
import com.tinexlab.tinocrm.security.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Table(name = "ventas")
@Getter
@Setter
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private Long id;

    @Column(name = "orden_venta")
    private String ordenVenta;
    @Column(name = "desc_venta")
    private String descVenta;

    @Column(name = "estado_venta")
    @ColumnDefault("'LEAD'")
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "fecha_reserva")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date fechaReserva;

    // asociaciones con cliente, factura y usuario
    @ManyToOne
    @JoinColumn(name="cliente_id", referencedColumnName = "cliente_id")
    @JsonManagedReference
    private Cliente cliente;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id", referencedColumnName = "factura_id")
    @JsonManagedReference
    private Factura factura;
    @ManyToOne
    @JoinColumn(name="usuario_id", referencedColumnName = "usuario_id")
    @JsonManagedReference
    private User user;

}
