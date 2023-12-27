package com.tinexlab.tinocrm.model.dto.request;

import com.tinexlab.tinocrm.util.EstadoCliente;
import com.tinexlab.tinocrm.util.TipoDoc;
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
