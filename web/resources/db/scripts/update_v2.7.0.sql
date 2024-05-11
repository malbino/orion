/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  malbino
 * Created: May 4, 2024
 */

INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES (67, 'Titulación > Egresados', '/titulacion/egresados/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (67, 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (67, 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (67, 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (67, 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (67, 8);