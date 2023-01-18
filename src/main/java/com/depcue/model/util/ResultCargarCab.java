package com.depcue.model.util;

import lombok.*;

import java.math.BigInteger;
import java.util.Date;


@Getter
@Setter
public class ResultCargarCab {
    private String codigoUnico;
    private Date fechaRegistro;
    private String usernameCarga;
    private BigInteger totalRegistros;

    public ResultCargarCab() {

    }
}
