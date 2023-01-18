CREATE OR REPLACE FUNCTION public.fn_upload_complete(pi_secuencial character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
declare 

reg tcliente%ROWTYPE;
total numeric(10,2);
numregs int4;
sec_sus int8;
num_logs int4;
num_abo int4;

begin
	num_logs = 0;

	--VALIDACIONES
	--verifica QR repetidos
	UPDATE tabonados_masive tms SET mensaje = 'El codigo QR '||t.qr||' se esta registrando mas de una vez'
	from(
		select
			tm.qr ,
			count(1) numreg
		from
			tabonados_masive tm
		where
			tm.code_unico = pi_secuencial
			and coalesce (tm.qr,'') != ''
		group by
			tm.qr) t 
	where
	 tms.qr = t.qr and t.numreg > 1
	and tms.code_unico = pi_secuencial;
	
	--verifica asientos repetidos
	UPDATE tabonados_masive tms SET 
		mensaje = CASE when mensaje!= NULL then  mensaje ||', El asiento '||t.asiento||' se esta registrando mas de una vez'
							ELSE ' El asiento '||t.asiento||' se esta registrando mas de una vez' end
	from 
		(
		select
		    sub_localidad,
			asiento ,
			count(1) numreg
		from
			tabonados_masive tm
		where
			code_unico = pi_secuencial
			and coalesce (tm.asiento,'') != ''
		group by sub_localidad,
			asiento) t
	where
	  t.numreg > 1 AND tms.asiento = t.asiento
		and  tms.code_unico = pi_secuencial;
	
	--que los abonos tengan MONTO
	UPDATE tabonados_masive tms SET 
		mensaje = CASE when mensaje!= NULL then  mensaje ||', ' else''end || ' El campo monto no tiene valor'
	where tms.code_unico = pi_secuencial and coalesce (cedula,'')!= '' and coalesce (monto ,'')= '';

--respalda los abonos
	INSERT INTO public.tabono_log
(cedula_abono, codigo_abono, estado, fecha_desde, fecha_hasta, forma_pago, nombre_abono, observacion, promocion, tipo_abono, valor_abono, casiento, cqrcode, csuscripcion, fecha_registro)
(select cedula_abono, codigo_abono, estado, fecha_desde, fecha_hasta, forma_pago, nombre_abono, observacion, promocion, tipo_abono, valor_abono, casiento, cqrcode, csuscripcion, now() 
from tabono);

delete from tabono;
	
	--UPDATES
	UPDATE public.tasiento set estado ='A';
	UPDATE public.tqrcode   set estado ='A';

	--asientos si es que ya existen
	UPDATE public.tasiento t set
	asiento = tm.asiento, localidad = tm.localidad, cod_localidad = tm.abre_localidad, sublocalidad = tm.sub_localidad
	from tabonados_masive tm 
		where
			tm.asiento  =  t.asiento 
			and tm.sub_localidad = t.sublocalidad
			and t.estado in ('A','O')
			and tm.code_unico = pi_secuencial
			and(tm.asiento !=''or tm.asiento !=null)
			and coalesce(tm.mensaje,'')='' ;

	--QRCodes si es que ya existen
	UPDATE public.tqrcode q set
			codigo  = tm.qr ,
			fecha_desde = now(), 
			cod_loc = tm.abre_localidad,
			serie = replace (tm.codigo_localidad,tm.abre_localidad||'-','')
	from 
			tabonados_masive tm  
		where  tm.qr  =  q.codigo 
			and tm.code_unico = pi_secuencial
			AND q.estado='A'
			and(tm.qr !=''or tm.qr !=null)
			AND coalesce(tm.mensaje,'')='';
	
	
	--INSERCIONES
	--inserta nuevos asientos en caso de no existir.
	INSERT INTO public.tasiento
	(asiento, columna, estado, fila, localidad, precio, sublocalidad, cod_localidad, tipo_area, zona)
	(select	
			tm.asiento ,
			null,
			'A',
			null,
			tm.localidad,
			0.0,
			tm.sub_localidad , 
			tm.abre_localidad,
			null,
			null
		from
			tabonados_masive tm
		where
			code_unico = pi_secuencial
			and tm.asiento  not in (select asiento from tasiento q 
									where q.asiento = tm.asiento 
									and q.sublocalidad = tm.sub_localidad  
									and q.estado in ('A','O'))
			and(tm.asiento !=''or tm.asiento !=null)
			AND coalesce(tm.mensaje,'')=''
	) ;

	--inserta nuevos QR en caso de no existir.
	insert into tqrcode  (codigo,estado , fecha_desde ,fecha_hasta, cod_loc, serie)
	(
		select
			qr ,
			'A',
			now(),
			to_date('2999-12-31','yyyy-mm-dd'), 
			abre_localidad,
			replace (codigo_localidad,abre_localidad||'-','')
		from
			tabonados_masive tm
		where
			code_unico = pi_secuencial
			and qr not in (select codigo from tqrcode q where q.estado='A')
			and (tm.qr != null or tm.qr !='')
			AND coalesce(tm.mensaje,'')=''
	) ;
	

----Valida si existe alguna observacion
	SELECT count(1) INTO num_logs
	  FROM tabonados_masive l
	WHERE l.code_unico = pi_secuencial
	AND coalesce(l.mensaje,'')!=''
	LIMIT 1;
	
	IF num_logs > 0 then
		--retorna en caso de existir alguna observacion
	  return 1;
	END IF;

return 0;
END
$function$
;
