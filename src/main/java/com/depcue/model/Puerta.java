package com.depcue.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Puerta")
public class Puerta  implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	@Id
	@Column(name = "id_puerta")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPuerta;

	@Column(name = "asignado")
	private boolean asignado = false;

	@Column(name = "numero", length = 100)
	private int numero;

	
	@Column(name = "descripcion", length = 500)
	private String descripcion;

	@Column(name = "tipo", length = 100)
	private String tipo; /* Entrada, Salida, Mixta*/

	

	// @OneToOne(mappedBy = "Puerta", cascade = CascadeType.ALL)
//    private Portero portero;
//
//	@OneToOne(mappedBy = "Puerta", cascade = CascadeType.ALL)
//	private StatePuerta tipoDePuerta;//tipo de puerta(Entrada,Salida,Entrada y Salida)	

	public Puerta(boolean asignado, int numero, String descripcion, String tipo) {
		super();
		this.asignado = asignado;
		this.numero = numero;
		this.descripcion = descripcion;
		this.tipo=tipo;

	}

	public Puerta() {
		super();
	}



	public Long getIdPuerta() {
		return idPuerta;
	}

	public void setIdPuerta(Long idPuerta) {
		this.idPuerta = idPuerta;
	}

	public boolean isAsignado() {
		return asignado;
	}

	public void setAsignado(boolean asignado) {
		this.asignado = asignado;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}


	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	


}
