package com.depcue.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.depcue.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

	List<Usuario> findByEstado(String estado);
	
	Optional<Usuario> findByEstadoAndUsername(String estado, String username);

//	@Query("Select  u from Usuario u join u.vehiculos v where u.estado = ?1  and v.estado = ?2 ")
	Optional<Usuario> findByEstadoAndId(String estado, Long id);
	
//	List<Usuario> findByEstadoAndVehiculosEstado(String estadou,String estadov);

}
