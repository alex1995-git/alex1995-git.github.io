package com.depcue.model;


import com.depcue.model.util.AbonoLst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="tsuscripcion")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Suscripcion implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long csuscripcion;

    private double valor;

    private int cantidad;

   // @ManyToOne
    //@OnDelete(action = OnDeleteAction.CASCADE)
    //
    //@JsonIgnore
    @ManyToOne
    //@JoinColumn(name = "ccliente", referencedColumnName = "ccliente")
    @JoinColumn(foreignKey = @ForeignKey(name = "ccliente"),name = "ccliente")
    private Cliente ccliente;

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
    private List<AbonoLst> listAbonos;

    @Transient
    private Boolean error= false;

    @Transient
    private String mensaje="";

    public Suscripcion() {

    }

    public Suscripcion(Boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public Suscripcion(Long csuscripcion) {
        this.csuscripcion = csuscripcion;
    }
}
