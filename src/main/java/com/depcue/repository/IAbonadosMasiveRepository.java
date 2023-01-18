package com.depcue.repository;

import com.depcue.model.AbonadosMasive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAbonadosMasiveRepository extends JpaRepository<AbonadosMasive, Long> {

    static String SQL_RESULT_CARGA_BY_ALL = "select code_unico codigoUnico,fecha_registro fechaRegistro,username_carga usernameCarga,count(*) totalRegistros  from tabonados_masive tm group by 1,2,3 order by fechaRegistro";
    static String SQL_RESULT_CARGA_BY_CODE = "select * from tabonados_masive where code_unico =:code_unico and coalesce(mensaje,'')='' order by id";

    static String SQL_RESULT_CARGA_NUM_ABO = "select count(*) from tabonados_masive where code_unico =:code_unico and cedula =:cedula";

    static String SQL_RESULT_CARGA_SUM_ABO = "select  sum(CAST (monto AS DOUBLE PRECISION))  from tabonados_masive where code_unico =:code_unico and cedula =:cedula and coalesce (monto,'')!= ''";


    @Query(value = "select fn_upload_complete(:pi_secuencial)", nativeQuery = true)
    public int procesarMigracionConsolidado(@Param("pi_secuencial") String guiMigracion);


    @Query(nativeQuery = true, value = SQL_RESULT_CARGA_BY_ALL)
    List findResultAll();

    @Query(nativeQuery = true, value = SQL_RESULT_CARGA_BY_CODE)
    List<AbonadosMasive> findResultByCode(@Param("code_unico") String code_unico);

    @Query(nativeQuery = true, value = SQL_RESULT_CARGA_NUM_ABO)
    int findNumByCodeUnicoAndCedula(@Param("code_unico") String code_unico, @Param("cedula") String cedula);

    @Query(nativeQuery = true, value = SQL_RESULT_CARGA_SUM_ABO)
    double findSumByCodeUnicoAndCedula(@Param("code_unico") String code_unico, @Param("cedula") String cedula);

    List<AbonadosMasive> findByCodeUnicoAndCedula(String codeUnico, String cedula);

}
