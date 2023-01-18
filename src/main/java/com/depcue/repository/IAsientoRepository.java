package com.depcue.repository;

import com.depcue.model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAsientoRepository extends JpaRepository<Asiento,Long>{

	List<Asiento> findByEstado(String estado);
	List<Asiento> findByLocalidadAndEstado(String localidad,String estado);
	Optional<Asiento> findByAsiento(String numAsiento);
	Optional<Asiento> findByAsientoAndEstado(String numAsiento,String Estado);

	Optional<Asiento> findByAsientoAndEstadoAndSublocalidad(String numAsiento,String Estado, String sublocalidad);

	List<Asiento> findByLocalidad(String localidad);

}
