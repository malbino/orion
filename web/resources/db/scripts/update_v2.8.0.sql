/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  malbino
 * Created: Aug 1, 2024
 */

INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES (68, 'Administrador > Plantillas', '/administrador/plantillas.xhtml');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (68, 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (68, 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (68, 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (68, 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES (68, 8);