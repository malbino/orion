ALTER TABLE `notapasantia`
	CHANGE COLUMN `OBSERVACIONES` `OBSERVACIONESEMPRESA` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_general_ci' AFTER `NOTATUTOR`;
ALTER TABLE `notapasantia`
	ADD COLUMN `INICIO` DATE NULL DEFAULT NULL AFTER `id_grupopasantia`;
ALTER TABLE `notapasantia`
	ADD COLUMN `FIN` DATE NULL DEFAULT NULL AFTER `INICIO`;
ALTER TABLE `notapasantia`
	ADD COLUMN `HORARIO` VARCHAR(255) NULL DEFAULT NULL AFTER `FIN`;
ALTER TABLE `notapasantia`
	ADD COLUMN `OBSERVACIONESTUTOR` VARCHAR(255) NULL DEFAULT NULL AFTER `HORARIO`;


INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES (64, 'Reportes > Pasantias > Carta de Solicitud', '/reportes/pasantias/cartaSolicitud/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(64, 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(64, 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(64, 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(64, 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(64, 8);

INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES (65, 'Reportes > Pasantias > Certificado de Pasantia', '/reportes/pasantias/certificadoPasantia/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(65, 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(65, 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(65, 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(65, 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(65, 8);

INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES (66, 'Reportes > Pasantias > Lista de Pasantias', '/reportes/pasantias/listaPasantias/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(66, 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(66, 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(66, 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(66, 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES
(66, 8);