package com.depcue.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tabono")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Abono  implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "codigo_abono", length = 1000)
	private String codigoAbono;

	@ManyToOne//(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(foreignKey = @ForeignKey(name = "csuscripcion"),name = "csuscripcion")
	//@JsonIgnore
	private Suscripcion csuscripcion;

	@ManyToOne//(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(foreignKey = @ForeignKey(name = "casiento"),name = "casiento")
	//@JsonIgnore
	private Asiento casiento;

	@ManyToOne//(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(foreignKey = @ForeignKey(name = "cqrcode"),name = "cqrcode")
	//@JsonIgnore
	private QRCode cqrcode;

	@Column(name = "estado", length = 1)
	private String estado;

	@Column(name = "valor_abono")
	private double valorAbono;
	
	@Column(name = "observacion", length = 1000)
	private String observacion;

	@Basic(optional = false)
	@Column(name = "fecha_desde", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaDesde;

	@Basic(optional = false)
	@Column(name = "fecha_hasta", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHasta;

	@Transient
	private boolean qrAutomatico = false;
	
    //@OneToMany(mappedBy = "abono",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@Where(clause = "estado = 'A'")
	@Transient
    private List<RegistroAbono> registrosAbonos;

	@Column(name = "cedula_abono", length = 20)
	private String cedulaAbono;

	@Column(name = "nombre_abono", length = 300)
	private String nombreAbono;

	@Column(name = "forma_pago", length = 50)
	private String forma_pago;

	@Column(name = "tipo_abono", length = 100)
	private String tipo_abono;
	
	@Column(name = "promocion", length = 100)
	private String promocion;
	@Transient
	private Boolean error= false;

	@Transient
	private String mensaje="";

	public Abono(Long id) {
		this.id = id;
	}

	public Abono(Boolean error, String mensaje) {
		this.error = error;
		this.mensaje = mensaje;
	}

	public Abono() {
		super();
		this.codigoAbono = "";
		this.estado = "A";
		this.observacion = "";
		this.registrosAbonos = new LinkedList<RegistroAbono>();;
	}


	public void setRegistrosAbonos(List<RegistroAbono> registrosAbonos) {
		for(RegistroAbono v : registrosAbonos) {
            v.setQr_abono(this.getCodigoAbono());
        }
		this.registrosAbonos = registrosAbonos;
	}

	public void addRegistrosAbonos(RegistroAbono registroAbono) {
		if (this.registrosAbonos == null) {
			this.registrosAbonos = new LinkedList<RegistroAbono>();
		}
		//registroAbono.setAbono(this);
		registroAbono.setQr_abono(this.getCodigoAbono());
		this.registrosAbonos.add(registroAbono);
	}
	
	
	
}
