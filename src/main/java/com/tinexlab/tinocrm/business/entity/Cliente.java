package com.tinexlab.tinocrm.business.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinexlab.tinocrm.business.util.EstadoCliente;
import com.tinexlab.tinocrm.business.util.TipoDoc;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long id;

    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @Column(name = "apellidos_cliente")
    private String apellidosCliente;

    @Column(name = "doc_id")
    private String docId;
    @Column(name = "tipo_doc")
    @ColumnDefault("'DNI'")
    @Enumerated(EnumType.STRING)
    private TipoDoc tipoDoc;

    @Column(name = "estado_cliente")
    @ColumnDefault("'INTERESADO'")
    @Enumerated(EnumType.STRING)
    private EstadoCliente estadoCliente;

    @Column(name = "fono_cliente")
    private String fonoCliente;
    @Column(name = "email_cliente")
    private String emailCliente;

    // un cliente puede haber participado de varias adquisiciones
    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // previene la validación innecesaria del Content-Type
    private List<Venta> ventas;

    @Override
    public String toString() {
        return "Cliente: " + this.nombreCliente + " " + this.apellidosCliente + " - Doc. Id: " + this.docId;
    }
}
