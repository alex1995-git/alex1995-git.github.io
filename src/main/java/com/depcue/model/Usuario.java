package com.depcue.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username", length = 200)
	private String username;
	
	@Column(name = "password", length = 2000)
	private String password;
	
	@Column(name = "role", length = 2000)
	private String role;
	
	@Column(name = "primer_nombre", length = 100)
	private String primerNombre;

	@Column(name = "segundo_nombre", length = 100)
	private String segundoNombre;

	@Column(name = "primer_apellido", length = 100)
	private String primerApellido;

	@Column(name = "segundo_apellido", length = 100)
	private String segundoApellido;

	@Column(name = "cedula", length = 100)
	private String cedula;

	@Column(name = "telefono", length = 100)
	private String telefono;

	@Column(name = "estado", length = 1)
	private String estado;

	public Usuario(String username, String password, String role  ,String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
			String cedula, String telefono) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.cedula = cedula;
		this.telefono = telefono;
		this.estado = "A";
	}

	public Usuario() {
		super();
		this.username= "";
		this.password ="";
		this.role="";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.segundoApellido = "";
		this.primerApellido = "";
		this.cedula = "";
		this.telefono = "";
		this.estado = "A";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
