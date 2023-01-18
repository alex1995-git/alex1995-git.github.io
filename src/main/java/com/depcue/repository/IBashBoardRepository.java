package com.depcue.repository;

import com.depcue.model.AbonadosMasive;
import com.depcue.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface IBashBoardRepository extends JpaRepository<Usuario, Long> {
    static String SQL_TOTAL_ABONADOS = "select code_unico codigoUnico,fecha_registro fechaRegistro,username_carga usernameCarga,count(*) totalRegistros  from tabonados_masive tm group by 1,2,3 order by fechaRegistro";
    static String SQL_TOTAL_SUSCRIPCIONES = "select * from tabonados_masive where code_unico =:code_unico order by secuencial";

    @Query(nativeQuery = true, value = SQL_TOTAL_ABONADOS)
    Integer findTotalAbonados();

    @Query(nativeQuery = true, value = SQL_TOTAL_SUSCRIPCIONES)
    Integer findTotalSubscripciones();

    @Query(nativeQuery = true, value = SQL_TOTAL_SUSCRIPCIONES)
    BigDecimal findTotalVentas();


}
