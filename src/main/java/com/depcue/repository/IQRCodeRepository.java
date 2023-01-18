package com.depcue.repository;

import com.depcue.model.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IQRCodeRepository extends JpaRepository<QRCode,Long>{

	static String SQL_QRCODE_MAX_CODE = "select max(serie) serie from tqrcode where cod_loc  = :alias";

	List<QRCode> findByEstado(String estado);

	Optional<QRCode> findByCodigo(String codigo);
	Optional<QRCode> findByCodigoAndEstado(String codigo, String estado);

	@Query(nativeQuery = true, value = SQL_QRCODE_MAX_CODE)
	Long findMaxCode(@Param("alias") String alias);


}
