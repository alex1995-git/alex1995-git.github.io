package com.depcue.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "registro_abono")
@Getter
@Setter
public class RegistroAbono  implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "resultqr", length = 1000)
	private String resultqr;

	@Column(name = "fecha_hora_entrada")
	private String fechaHoraEntrada;
	
	@Column(name = "fecha_hora_salida")
	private String fechaHoraSalida;

	@Column(name = "estado", length = 1)
	private String estado;

	
	/*@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(foreignKey = @ForeignKey(name = "abono_id"),name = "abono_id", nullable = true, updatable = true)
	@JsonIgnore*/
	private String qr_abono;
	
	
	public RegistroAbono() {
		super();
		this.resultqr = "";
		this.fechaHoraEntrada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(); 
		this.fechaHoraSalida= null;
		//this.abono= new Abono();
		this.qr_abono="";
		this.estado="A";
	}
	
	public RegistroAbono(String resultqr ) {
		super();
		this.resultqr = resultqr;
		this.fechaHoraEntrada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
		this.fechaHoraSalida= null;
		//this.abono= new Abono();
		this.qr_abono="";
		this.estado="A";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResultqr() {
		return resultqr;
	}

	public void setResultqr(String resultqr) {
		this.resultqr = resultqr;
	}

	/*public Abono getAbono() {
		return abono;
	}

	public void setAbono(Abono abono) {
		this.abono = abono;
	}*/

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/*public Long getidAbono() {
		return abono.getId();
	}*/

	public String getFechaHoraEntrada() {
		return fechaHoraEntrada;
	}

	public void setFechaHoraEntrada(String fechaHoraEntrada) {
		this.fechaHoraEntrada = fechaHoraEntrada;
	}

	public String getFechaHoraSalida() {
		return fechaHoraSalida;
	}

	public void setFechaHoraSalida(String fechaHoraSalida) {
		this.fechaHoraSalida = fechaHoraSalida;
	}

}
