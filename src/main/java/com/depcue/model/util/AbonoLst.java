package com.depcue.model.util;

import com.depcue.model.Asiento;
import com.depcue.model.QRCode;
import com.depcue.model.RegistroAbono;
import com.depcue.model.Suscripcion;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
public class AbonoLst {

    private String codigoAbono;

    private Long asiento;

    private Long cqrcode;

    private double valorAbono;

    private String observacion;

    private boolean qrAutomatico = false;

}
