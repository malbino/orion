/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  malbino
 * Created: Sep 1, 2024
 */

INSERT INTO `recurso` (`ID_RECURSO`, `NOMBRE`, `URLPATTERN`) VALUES ((SELECT MAX(r.id_recurso)+1 FROM recurso r), 'Horarios > Aulas', '/horarios/aula/');
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 1);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 4);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 6);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 7);
INSERT INTO `privilegio` (`id_recurso`, `id_rol`) VALUES ((SELECT MAX(r.id_recurso) FROM recurso r), 8);