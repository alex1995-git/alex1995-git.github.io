
CREATE TABLE IF NOT EXISTS public.tabono_log (
	id bigserial NOT NULL,
	cedula_abono varchar(20) NULL,
	codigo_abono varchar(1000) NULL,
	estado varchar(1) NULL,
	fecha_desde timestamp NOT NULL,
	fecha_hasta timestamp NOT NULL,
	forma_pago varchar(50) NULL,
	nombre_abono varchar(300) NULL,
	observacion varchar(1000) NULL,
	promocion varchar(100) NULL,
	tipo_abono varchar(100) NULL,
	valor_abono float8 NULL,
	casiento int8 NOT NULL,
	cqrcode int8 NOT NULL,
	csuscripcion int8 NOT null,
	fecha_registro timestamp NOT NULL
);

