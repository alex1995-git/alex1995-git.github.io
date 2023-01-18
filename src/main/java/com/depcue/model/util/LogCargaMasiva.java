package com.depcue.model.util;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tlog_carga_masiva")
@Getter
@Setter
public class LogCargaMasiva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clogcargamasiva;

    private String secuencial;

    private String observacion;
}
