package com.depcue.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tabonados_masive")
@Getter
@Setter
public class AbonadosMasive implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "secuencial")
    private String secuencial;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "sub_localidad")
    private String subLocalidad;

    @Column(name = "abre_localidad")
    private String abreLocalidad;

    @Column(name = "codigo_localidad")
    private String codigoLocalidad;

    @Column(name = "qr")
    private String qr;

    @Column(name = "tarifa")
    private String tarifa;

    @Column(name = "tipo_tarifa")
    private String tipoTarifa;

    @Column(name = "tipo_cliente")
    private String tipoCliente;

    @Column(name = "asiento")
    private String asiento;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "cedula")
    private String cedula;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "observacion")
    private String observacion;

    @Basic(optional = false)
    @Column(name = "fecha_registro", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(name = "code_unico", nullable = false)
    private String codeUnico;

    @Column(name = "username_carga")
    private String usernameCarga;

    @Column(name = "cedula_abono")
    private String cedulaAbono;

    @Column(name = "nombre_abono")
    private String nombreAbono;

    @Column(name = "fecha_subscripcion")
    private String fechaSubscripcion;

    @Column(name = "forma_pago")
    private String formaPago;

    @Column(name = "monto")
    private String monto;

    @Column(name = "promocion")
    private String promocion;

    @Column(name = "error")
    private Boolean error;

    @Column(name = "mensaje")
    private String mensaje;

    public void setMensaje(String mensaje){
        if(this.mensaje !=null && !this.mensaje.isEmpty()){
            mensaje = this.mensaje + ", "+mensaje;
            System.out.println(mensaje);
        }
        this.mensaje = mensaje;
    }




}
