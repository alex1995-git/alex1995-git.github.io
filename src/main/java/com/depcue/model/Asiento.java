package com.depcue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tasiento")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Asiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long casiento;

    @Column(name = "asiento")
    private String asiento;

    private String zona;

    private String fila;

    private String columna;

    @Column(length = 50)
    private String localidad;

    @Column(length = 100)
    private String sublocalidad;

    @Column(length = 4)
    private String codLocalidad;

    @Column(name = "tipo_area")
    private String tipoArea; // G=grada, P=puerta;pared;pasillo, A=asi ento

    @Column(name = "estado", length = 1)
    private String estado;

    private double precio;

    @Transient
    private Boolean error = false;

    @Transient
    private String mensaje = "";

    public Asiento() {

    }

    public Asiento(Boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }
}
