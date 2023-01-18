package com.depcue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tcliente")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente extends Persona implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	@Id
	@Column(name = "ccliente")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ccliente;

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


	@Column(name = "observacion", length = 255)
	private String observacion;

	@Column(name = "secuencial_upload", length = 50)
	private String secuencialUpload;

	@Transient
	private Boolean error= false;

	@Transient
	private String mensaje="";

	public Cliente() {

	}

	public Cliente(Long ccliente) {
		this.ccliente = ccliente;
	}

	public Cliente(Boolean error, String mensaje) {
		this.error = error;
		this.mensaje = mensaje;
	}
}
