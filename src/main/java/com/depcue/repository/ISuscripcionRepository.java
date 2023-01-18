package com.depcue.repository;

import com.depcue.model.Cliente;
import com.depcue.model.Suscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ISuscripcionRepository extends JpaRepository<Suscripcion,Long>{

	Page<Suscripcion> findByEstado(String estado, Pageable pageable);

	Optional<Suscripcion> findByEstadoAndCsuscripcion(String estado, Long csuscripcion);

	List<Suscripcion> findByCcliente(Cliente ccliente);

	Optional<Suscripcion> findByCclienteAndEstado(Cliente ccliente, String estado);


}
