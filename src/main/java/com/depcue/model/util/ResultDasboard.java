package com.depcue.model.util;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Getter
@Setter
public class ResultDasboard {

    private Integer totalAbonados;
    private Integer totalSusCripciones;
    private BigDecimal totalVentas;


    private Boolean error = false;


    private String mensaje = "";

    public ResultDasboard() {

    }

    public ResultDasboard(Boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }
}
