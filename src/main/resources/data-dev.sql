delete from usuario where id in (1,2,3,4,5);


INSERT INTO public.usuario
(id, cedula, estado, "password", primer_apellido, primer_nombre, "role", segundo_apellido, segundo_nombre, telefono, username)
VALUES(1, '', 'A', '$2a$10$vZ6f5vCSDcAs1LMMhE5yru4awYHyG6F2dJ2Li.4AS7VFQWtbIwkYO', '', '', 'ADMIN', '', '', '', 'admin');
INSERT INTO public.usuario
(id, cedula, estado, "password", primer_apellido, primer_nombre, "role", segundo_apellido, segundo_nombre, telefono, username)
VALUES(2, '', 'A', '$2a$10$wwK8LscM4HZR0bX7lg71b.z/rsak82Itpug.wJrB2d3TOfVZNU61m', '', '', 'USER', '', '', '', 'palco');
INSERT INTO public.usuario
(id, cedula, estado, "password", primer_apellido, primer_nombre, "role", segundo_apellido, segundo_nombre, telefono, username)
VALUES(3, '', 'A', '$2a$10$/MhXpEGnIA2TLL9/.m3Cneoy5/3CyTIjke0qhpBt8RhnIRkepe2hm', '', '', 'USER', '', '', '', 'general');
INSERT INTO public.usuario
(id, cedula, estado, "password", primer_apellido, primer_nombre, "role", segundo_apellido, segundo_nombre, telefono, username)
VALUES(4, '', 'A', '$2a$10$SMIU2lVs4IQ4RxTj9mcPA.kZLoQ2mXFXNvNpUPj2Rdug2dOrmJWIq', '', '', 'USER', '', '', '', 'tribuna');
INSERT INTO public.usuario
(id, cedula, estado, "password", primer_apellido, primer_nombre, "role", segundo_apellido, segundo_nombre, telefono, username)
VALUES(5, '', 'A', '$2a$10$P6mwL5/KrPWKcjlzwa81F.1dOmvX7WC8Ty/Z0skD7RckCtbeJIE3O', '', '', 'USER', '', '', '', 'preferencia');

