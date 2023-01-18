package com.depcue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depcue.model.RegistroAbono;

public interface IRegistroAbonoRepository extends JpaRepository<RegistroAbono, Long>{

	List<RegistroAbono> findByEstado(String estado);
	
	//List<RegistroAbono> findByEstadoAndAbonoId(String estado,Long id);
	
	List<RegistroAbono> findByEstadoAndResultqr(String estado,String resultqr);
	
}
