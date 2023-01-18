package com.depcue.repository;

import com.depcue.model.AbonadosMasive;
import com.depcue.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IClienteRepository extends JpaRepository<Cliente, Long> {

    static String SQL_RESULT_CLIENTE_REG = "select * from tcliente where estado = :estado and identificacion in (select cedula from tabonados_masive)";
    static String SQL_SEARCH_CLIENT = "select  * from tcliente where (upper(trim(coalesce(correo,'')) ||  trim(coalesce(identificacion ,''))   ||  trim(coalesce(nombres  ,''))  ||  trim(coalesce(telefono  ,''))))  like %:value%";


    Page<Cliente> findByEstado(String estado, Pageable pageable);

    Optional<Cliente> findByEstadoAndCcliente(String estado, Long ccliente);

    List<Cliente> findByIdentificacion(String cedula);

    Optional<Cliente> findByIdentificacionAndEstado(String cedula, String estado);

    @Query(nativeQuery = true, value = SQL_RESULT_CLIENTE_REG)
    List<Cliente> findByEstadoAndCarga(@Param("estado") String estado);


    @Query(nativeQuery = true, value = SQL_SEARCH_CLIENT)
	Page<Cliente> findBySearchClient(@Param("value") String value, Pageable pageable);


}
