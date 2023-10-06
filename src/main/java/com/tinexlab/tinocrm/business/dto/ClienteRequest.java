package com.tinexlab.tinocrm.business.dto;

import com.tinexlab.tinocrm.business.util.EstadoCliente;
import com.tinexlab.tinocrm.business.util.TipoDoc;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequest {
    private String nombreCliente;
    private String apellidosCliente;
    private String docId;
    @Enumerated(EnumType.STRING)
    private TipoDoc tipoDoc;
    @Enumerated(EnumType.STRING)
    private EstadoCliente estadoCliente;
    private String fonoCliente;
    private String emailCliente;
}
