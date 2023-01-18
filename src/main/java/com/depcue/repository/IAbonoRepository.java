package com.depcue.repository;

import java.util.List;
import java.util.Optional;

import com.depcue.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import com.depcue.model.Abono;
import com.depcue.model.Usuario;

public interface IAbonoRepository extends JpaRepository<Abono,Long>{

	List<Abono> findByEstado(String estado);

	Optional<Abono> findByEstadoAndId(String estado, Long id);

	Optional<Abono> findByCodigoAbono(String codigoAbono);

	List<Abono>  findByCsuscripcion(Suscripcion csuscripcion);

	//Optional<Abono> findByUrlqr(String urlqr);
	
	Optional<Abono> findByEstadoAndCodigoAbono (String estado,String codigoAbono);
	
	//Optional<Abono> findByEstadoAndUrlqrAndCodigoAbono (String estado, String urlqr,String codigoAbono);
	
	//List<Abono> findByEstadoAndCodigoAbonoAndLocalidadAndAsiento(String estado, String codigoAbono ,String localidad, String asiento);

}
