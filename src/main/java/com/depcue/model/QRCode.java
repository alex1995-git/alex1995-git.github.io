package com.depcue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tqrcode")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QRCode implements Serializable {
    @Id
    @Column(name = "cqrcode")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cqrcode;

    @Column(name = "cod_loc", length = 5)
    private String codLoc;

    @Column(name = "serie", length = 5)
    private String serie;

    @Column(name = "codigo", length = 1000)
    private String codigo;

    @Basic(optional = false)
    @Column(name = "fecha_desde", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;

    @Basic(optional = false)
    @Column(name = "fecha_hasta", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;

    @Column(name = "estado", length = 1)
    private String estado;

    @Transient
    private Boolean error= false;

    @Transient
    private String mensaje="";

    public QRCode() {

    }

    public QRCode(Boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public QRCode(Long cqrcode) {
        this.cqrcode = cqrcode;
    }
}
