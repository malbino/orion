/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  malbino
 * Created: Sep 19, 2024
 */

-- actualizando permisos
UPDATE `recurso` SET NOMBRE='Reportes > Centralizadores > Centralizador Calificaciones', URLPATTERN='/reportes/centralizadores/centralizadorCalificaciones/' WHERE NOMBRE='Reportes > Centralizador Calificaciones';
UPDATE `recurso` SET NOMBRE='Reportes > Centralizadores > Centralizador Calificaciones PRAE', URLPATTERN='/reportes/centralizadores/centralizadorCalificacionesPRAE/' WHERE NOMBRE='Reportes > Centralizador Calificaciones PRAE';

INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.ID_RECURSO)+1 FROM `recurso` r), 'Reportes > Centralizadores > Centralizador Calificaciones Homologación', '/reportes/centralizadores/centralizadorCalificacionesHomologacion/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.ID_RECURSO) FROM `recurso` r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.ID_RECURSO) FROM `recurso` r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.ID_RECURSO) FROM `recurso` r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.ID_RECURSO) FROM `recurso` r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.ID_RECURSO) FROM `recurso` r), 8);