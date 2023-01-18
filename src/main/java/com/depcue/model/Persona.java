package com.depcue.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class Persona implements Serializable {

    @Column(name = "identificacion", length = 20)
    private String identificacion;

    @Column(name = "tipo_identificacion", length = 1)
    private Character tipoIdentificacion;

    @Column(name = "nombres", length = 1000)
    private String nombres;

    @Column(name = "fecha_nacimiento", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", length = 1000)
    private String direccion;
}
